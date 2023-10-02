from django.urls import path

from .views import (
    BranchesAPIView,
    BranchDetailAPIView,
    BanksAPIView,
    BankDetailAPIView,
    CreateAccountAPIView,
    AccountListAPIView,
    DepositAPIView,
    WithdrawAPIView,
    AccountDetailAPIView
    
)

urlpatterns = [
    path('branches/', BranchesAPIView.as_view(),name='branches'),
    path('branch/<int:pk>/', BranchDetailAPIView.as_view(),name='branch-detail'),
    path('banks/', BanksAPIView.as_view(), name='banks'),
    path('bank/<int:pk>/', BankDetailAPIView.as_view(), name='bank-detail'),
    path('create_account/', CreateAccountAPIView.as_view(), name='create-account'),
    path('deposits/', DepositAPIView.as_view(), name= 'deposits'),
    path('withdrawals/', WithdrawAPIView.as_view(), name= 'withdrawals'),
    path('accounts/', AccountListAPIView.as_view(), name='accounts'),
    path('account/<int:pk>/', AccountDetailAPIView.as_view(), name='account-detail')
]