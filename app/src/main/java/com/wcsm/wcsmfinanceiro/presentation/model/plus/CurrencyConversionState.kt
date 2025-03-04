package com.wcsm.wcsmfinanceiro.presentation.model.plus

data class CurrencyConversionState(
    var baseCode: String = "",
    var baseCodeErrorMessage: String = "",
    var targetCode: String = "",
    var targetCodeErrorMessage: String = "",
    var valueToConvert: Double = 0.0,
    var valueToConvertErrorMessage: String = "",
    var convertedValue: Double = 0.0
)