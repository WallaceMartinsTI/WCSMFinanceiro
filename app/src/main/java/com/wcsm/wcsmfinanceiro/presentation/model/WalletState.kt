package com.wcsm.wcsmfinanceiro.presentation.model

data class WalletState(
    var walletId: Long = 0,
    var title: String = "",
    var titleErrorMessage: String = "",
    var balance: Double = 0.0,
    var balanceErrorMessage: String = "",
    var blocked: Boolean = false
)