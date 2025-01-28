package com.wcsm.wcsmfinanceiro.presentation.model

data class WalletCardState(
    var walletCardId: Long = 0,
    var title: String = "",
    var titleErrorMessage: String = "",
    var limit: Double = 0.0,
    var limitErrorMessage: String = "",
    var spent: Double = 0.0,
    var spentErrorMessage: String = "",
    var available: Double = 0.0,
)
