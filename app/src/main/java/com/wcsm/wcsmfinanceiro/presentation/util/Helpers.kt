package com.wcsm.wcsmfinanceiro.presentation.util

import com.wcsm.wcsmfinanceiro.domain.entity.BillType
import com.wcsm.wcsmfinanceiro.domain.entity.PaymentType

fun getPaymentTypeFromString(displayName: String) : PaymentType? {
    return PaymentType.entries.find { it.displayName == displayName }
}

fun getBillTypeFromString(displayName: String) : BillType? {
    return BillType.entries.find { it.displayName == displayName }
}