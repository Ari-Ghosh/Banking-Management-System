from .models import UserAccount, Withdrawal, Deposit, Transfer
from .serializers import UserAccountSerializer
from rest_framework.views import APIView
from rest_framework import viewsets
from rest_framework import status
from rest_framework.response import Response
import random

class UserRegistrationView(APIView):
    def generate_account_number(self):
        while True:
            account_number = random.randint(100000000000, 999999999999)  # Reduced the range for a 6-digit account number
            if not UserAccount.objects.filter(account_number=account_number).exists():
                return account_number

    def post(self, request):
        # Extract user registration data from the request
        name = request.data.get('name')
        address = request.data.get('address')
        email = request.data.get('email')
        phone_number = request.data.get('phone_number')
        account_type = request.data.get('account_type')
        mpin = request.data.get('mpin')
        

        try:
            # Create a new Registration instance
            registration = UserAccount.objects.create(
                name=name, address=address, email=email, phone_number=phone_number,
                account_type=account_type, mpin=mpin, account_number=100000000000
            )

            # Create an associated bank account
            account_number = self.generate_account_number()
            registration.account_number = account_number
            registration.save()

            return Response({'detail': "Registration successful", 'account_number': account_number, "mpin": mpin}, status=status.HTTP_201_CREATED)
        except Exception as e:
            return Response({'error': str(e)}, status=status.HTTP_400_BAD_REQUEST)

class UserLoginView(APIView):
    def post(self, request):
        # Extract account number and MPIN from the request
        account_number = request.data.get('account_number')
        mpin = request.data.get('mpin')

        # Authenticate the user based on account number and MPIN
        registration = self.authenticate_user(account_number, mpin)

        if registration is not None:
            # Authentication successful, you can return a token or other login-related data
            return Response({'detail': "Authentication successful"}, status=status.HTTP_200_OK)
        else:
            # Authentication failed, return an error response
            return Response({'detail': 'Invalid account number or MPIN'}, status=status.HTTP_400_BAD_REQUEST)

    def authenticate_user(self, account_number, mpin):
        # Implement your custom authentication logic here
        try:
            # Query the database to find the user profile based on the provided account number
            registration = UserAccount.objects.get(account_number=account_number)
            
            # Check if the provided MPIN matches the user's MPIN
            if registration.mpin == mpin:
                return registration
            else:
                return None  # MPIN doesn't match
        except UserAccount.DoesNotExist:
            return None  # User with the provided account number doesn't exist

class AccountViewSet(viewsets.ModelViewSet):
    queryset = UserAccount.objects.all()
    serializer_class = UserAccountSerializer

    # Implement other CRUD operations (create, retrieve, update, destroy) as needed

class AccountBalanceView(APIView):
    def get(self, request, account_number):
        try:
            registration = UserAccount.objects.get(account_number=account_number)
        except UserAccount.DoesNotExist:
            return Response({'detail': 'Account not found'}, status=status.HTTP_404_NOT_FOUND)

        balance = registration.balance

        return Response({'account_number': account_number, 'balance': balance}, status=status.HTTP_200_OK)
    
class AccountDetailsView(APIView):
    def get(self, request, account_number):
        try:
            registration = UserAccount.objects.get(account_number=account_number)
        except UserAccount.DoesNotExist:
            return Response({'detail': 'Account not found'}, status=status.HTTP_404_NOT_FOUND)

        serializer = UserAccountSerializer(registration)

        return Response(serializer.data, status=status.HTTP_200_OK)

class WithdrawalView(APIView):
    def post(self, request):
        # Extract account_number and amount from the request
        account_number = request.data.get('account_number')
        amount = request.data.get('amount')

        # Retrieve the associated account
        try:
            registration = UserAccount.objects.get(account_number=account_number)
        except UserAccount.DoesNotExist:
            return Response({'detail': 'Account not found'}, status=status.HTTP_404_NOT_FOUND)

        # Check if the account has sufficient balance for the withdrawal
        if registration.balance < amount:
            return Response({'detail': 'Insufficient balance'}, status=status.HTTP_400_BAD_REQUEST)

        # Create a withdrawal record
        Withdrawal.objects.create(registration=registration, amount=amount)

        return Response({'detail': 'Withdrawal successful'}, status=status.HTTP_200_OK)

class DepositView(APIView):
    def post(self, request):
        # Extract account_number and amount from the request
        account_number = request.data.get('account_number')
        amount = request.data.get('amount')

        # Retrieve the associated account
        try:
            registration = UserAccount.objects.get(account_number=account_number)
        except UserAccount.DoesNotExist:
            return Response({'detail': 'Account not found'}, status=status.HTTP_404_NOT_FOUND)

        # Create a deposit record
        Deposit.objects.create(registration=registration, amount=amount)

        return Response({'detail': 'Deposit successful'}, status=status.HTTP_200_OK)
    
class TransferView(APIView):
    def post(self, request):
        # Extract sender_account_number, receiver_account_number, and amount from the request
        sender_account_number = request.data.get('sender_account_number')
        receiver_account_number = request.data.get('receiver_account_number')
        amount = request.data.get('amount')

        # Retrieve the associated sender and receiver accounts
        try:
            sender_registration = UserAccount.objects.get(account_number=sender_account_number)
            receiver_registration = UserAccount.objects.get(account_number=receiver_account_number)
        except UserAccount.DoesNotExist:
            return Response({'detail': 'Account not found'}, status=status.HTTP_404_NOT_FOUND)

        # Check if the sender account has sufficient balance for the transfer
        if sender_registration.balance < amount:
            return Response({'detail': 'Insufficient balance'}, status=status.HTTP_400_BAD_REQUEST)

        # Create a transfer record
        transfer = Transfer(sender_registration=sender_registration, receiver_registration=receiver_registration, amount=amount)
        transfer.save()

        # Perform withdrawal from sender's account
        Withdrawal.objects.create(registration=sender_registration, amount=amount)

        # Perform deposit to receiver's account
        Deposit.objects.create(registration=receiver_registration, amount=amount)

        return Response({'detail': 'Transfer successful'}, status=status.HTTP_200_OK)
    