from django.urls import path, include
from rest_framework.routers import DefaultRouter
from .views import AccountDetailsView, AccountBalanceView, UserLoginView, UserRegistrationView, AccountViewSet, WithdrawalView, DepositView, TransferView

# Create a router for the AccountViewSet
account_router = DefaultRouter()
account_router.register(r'', AccountViewSet)

urlpatterns = [
    path('login/', UserLoginView.as_view(), name='user-login'),
    path('register/', UserRegistrationView.as_view(), name='user-register'),
    path('accounts/', include(account_router.urls)),  # Include the account_router URLs
    path('account/<str:account_number>/', AccountDetailsView.as_view(), name='account-details'),
    path('balance/<str:account_number>/', AccountBalanceView.as_view(), name='account-balance'),
    path('withdraw/', WithdrawalView.as_view(), name='withdraws'),
    path('deposit/', DepositView.as_view(), name='deposits'),
    path('transfer/', TransferView.as_view(), name='transfers'),
]
