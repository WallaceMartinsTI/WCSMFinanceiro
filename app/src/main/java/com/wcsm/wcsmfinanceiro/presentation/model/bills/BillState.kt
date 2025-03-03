package com.wcsm.wcsmfinanceiro.presentation.model.bills

import com.wcsm.wcsmfinanceiro.data.local.model.BillType
import com.wcsm.wcsmfinanceiro.data.local.model.PaymentType

data class BillState(
    var billId: Long = 0,
    var billType: BillType = BillType.INCOME,
    var origin: String = "",
    var title: String = "",
    var titleErrorMessage: String = "",
    var date: Long = 0L,
    var dateErrorMessage: String = "",
    var description: String = "",
    var value: Double = 0.0,
    var valueErrorMessage: String = "",
    var paymentType: PaymentType = PaymentType.MONEY,
    var category: String = "",
    var categoryErrorMessage: String = "",
    var paid: Boolean = false,
    var dueDate: Long = 0L,
    var expired: Boolean = false,
    var tags: List<String> = emptyList(),
    var responseErrorMessage: String = ""
)
