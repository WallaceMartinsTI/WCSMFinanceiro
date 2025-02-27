package com.wcsm.wcsmfinanceiro.presentation.model.wallet

sealed class WalletOperationType(val walletType: WalletType) {
    class Save(walletType: WalletType) : WalletOperationType(walletType)
    class Update(walletType: WalletType) : WalletOperationType(walletType)
    class Delete(walletType: WalletType) : WalletOperationType(walletType)
}