package com.wcsm.wcsmfinanceiro.presentation.util

import com.wcsm.wcsmfinanceiro.domain.model.PaymentType

fun getPaymentTypeFromString(displayName: String) : PaymentType? {
    return PaymentType.entries.find { it.displayName == displayName }
}
