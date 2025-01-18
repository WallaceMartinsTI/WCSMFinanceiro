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
    return tagsDividedByComma.split(",")
}