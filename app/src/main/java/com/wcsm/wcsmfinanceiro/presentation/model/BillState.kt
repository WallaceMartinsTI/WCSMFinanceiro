package com.wcsm.wcsmfinanceiro.presentation.model

import com.wcsm.wcsmfinanceiro.data.model.BillType
import com.wcsm.wcsmfinanceiro.data.model.PaymentType

data class BillState(
    var id: Long = 0,
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
    var paid: Boolean = false,
    var dueDate: Long = 0L,
    var expired: Boolean = false,
    var tags: List<String> = emptyList(),
    var isLoading: Boolean = false
)
