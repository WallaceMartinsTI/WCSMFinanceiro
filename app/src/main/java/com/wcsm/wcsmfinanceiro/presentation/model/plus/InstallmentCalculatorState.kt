package com.wcsm.wcsmfinanceiro.presentation.model.plus

data class InstallmentCalculatorState(
    var value: Double = 0.0,
    var valueErrorMessage: String = "",
    var installment: Int = 0,
    var installmentErrorMessage: String = "",
    var fees: String = "",
    var feesErrorMessage: String = "",
    var installmentCalculationResult: String = "",
    var installmentTotalWithFees: String = ""
)
