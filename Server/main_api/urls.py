from django.urls import path

from .views import (
    BranchesAPIView,
    BranchDetailAPIView,
    BanksAPIView,
    BankDetailAPIView,
    CreateAccountAPIView,
    AccountListAPIView

)

urlpatterns = [
    path('branches/', BranchesAPIView.as_view(),name='branches'),
    path('branch/(?P<pk>[0-9]+)/', BranchDetailAPIView.as_view(),name='branch-detail'),
    path('banks', BanksAPIView.as_view(), name='banks'),
    path('bank/(?P<pk>[0-9]+)/', BankDetailAPIView.as_view(), name='bank-detail'),
    path('create_account/', CreateAccountAPIView.as_view(), name='create-account'),
    path('accounts/', AccountListAPIView.as_view(), name='accounts')
]