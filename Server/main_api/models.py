from django.db import models
from django.utils import timezone

class UserAccount(models.Model):
    name = models.CharField(max_length=255)
    address = models.CharField(max_length=255)
    email = models.EmailField()
    phone_number = models.CharField(max_length=10)
    open_date = models.DateTimeField(default=timezone.now)
    account_type = models.CharField(max_length=3, choices=[('SAV', 'Savings'), ('CUR', 'Current')], default='SAV')
    account_number = models.PositiveBigIntegerField(unique=True)
    mpin = models.PositiveIntegerField()

    @property
    def balance(self):
        deposits = sum([deposit.amount for deposit in self.deposits.all()])
        withdrawals = sum([withdrawal.amount for withdrawal in self.withdrawals.all()])
        total = deposits - withdrawals
        return total

    def __str__(self):
        return str(self.account_number)

class Withdrawal(models.Model):
    registration = models.ForeignKey(UserAccount, on_delete=models.CASCADE, default=100000000000, related_name='withdrawals')
    amount = models.DecimalField(max_digits=10, decimal_places=2)
    timestamp = models.DateTimeField(default=timezone.now)

    def __str__(self):
        return f"Withdrawal from Account {self.registration.account_number}: ${self.amount}"

class Deposit(models.Model):
    registration = models.ForeignKey(UserAccount, on_delete=models.CASCADE, default=100000000000, related_name='deposits')
    amount = models.DecimalField(max_digits=10, decimal_places=2)
    timestamp = models.DateTimeField(default=timezone.now)

    def __str__(self):
        return f"Deposit to Account {self.registration.account_number}: ${self.amount}"

class Transfer(models.Model):
    sender_registration = models.ForeignKey(UserAccount, on_delete=models.CASCADE, default=100000000000, related_name='transfers_sent')
    receiver_registration = models.ForeignKey(UserAccount, on_delete=models.CASCADE, default=100000000000, related_name='transfers_received')
    amount = models.DecimalField(max_digits=10, decimal_places=2)
    timestamp = models.DateTimeField(default=timezone.now)

    def __str__(self):
        return f"Transfer from Account {self.sender_registration.account_number} to Account {self.receiver_registration.account_number}: ${self.amount}"
