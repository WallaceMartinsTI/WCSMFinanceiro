package com.wcsm.wcsmfinanceiro.presentation.util

import com.wcsm.wcsmfinanceiro.data.model.BillType
import com.wcsm.wcsmfinanceiro.data.model.PaymentType

fun getPaymentTypeFromString(displayName: String) : PaymentType {
    return PaymentType.entries.find { it.displayName == displayName } ?: PaymentType.MONEY
}

fun getBillTypeFromString(displayName: String) : BillType {
    return BillType.entries.find { it.displayName == displayName } ?: BillType.INCOME
}

fun getFormattedTags(tagsDividedByComma: String) : List<String> {
    return tagsDividedByComma.split(",").map { it.trim() }
}

fun getDoubleForStringPrice(price: String) : Double {
    var formatedPrice = if(price.length == 1) { // 3 -> 0,03
        "0,0$price"
    } else if(price.length == 2) { // 30 -> 0,30
        "0,$price"
    } else {
        formatNumberWithComma(price)
    }

    formatedPrice = formatedPrice.replace(",", ".")

    return formatedPrice.toDoubleOrNull() ?: 0.0
}

private fun formatNumberWithComma(stringNumber: String) : String {
    if(stringNumber.length < 3) return stringNumber

    val prefix = stringNumber.dropLast(2)
    val suffix = stringNumber.takeLast(2)
    return "$prefix,$suffix"
}