# from rest_framework import serializers
# from .models import UserProfile, Account, Withdrawal, Deposit, Transfer

# class UserProfileSerializer(serializers.ModelSerializer):
#     class Meta:
#         model = UserProfile
#         fields = '__all__'

# class AccountSerializer(serializers.ModelSerializer):
#     user_profile = UserProfileSerializer()  # Nested serializer to include user profile details

#     class Meta:
#         model = Account
#         fields = '__all__'

# class WithdrawalSerializer(serializers.ModelSerializer):
#     class Meta:
#         model = Withdrawal
#         fields = '__all__'

# class DepositSerializer(serializers.ModelSerializer):
#     class Meta:
#         model = Deposit
#         fields = '__all__'

# class TransferSerializer(serializers.ModelSerializer):
#     class Meta:
#         model = Transfer
#         fields = '__all__'


from rest_framework import serializers
from .models import UserAccount, Withdrawal, Deposit, Transfer

class UserAccountSerializer(serializers.ModelSerializer):
    class Meta:
        model = UserAccount
        fields = '__all__'

class WithdrawalSerializer(serializers.ModelSerializer):
    class Meta:
        model = Withdrawal
        fields = '__all__'

class DepositSerializer(serializers.ModelSerializer):
    class Meta:
        model = Deposit
        fields = '__all__'

class TransferSerializer(serializers.ModelSerializer):
    class Meta:
        model = Transfer
        fields = '__all__'
