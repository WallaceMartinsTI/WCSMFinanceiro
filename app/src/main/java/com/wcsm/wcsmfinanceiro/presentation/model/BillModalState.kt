package com.wcsm.wcsmfinanceiro.presentation.model

import com.wcsm.wcsmfinanceiro.domain.model.Category
import com.wcsm.wcsmfinanceiro.domain.model.PaymentType

data class BillModalState(
    val id: Long,
    val origin: String,
    val title: String,
    val titleErrorMessage: String,
    val date: Long,
    val dateErrorMessage: String,
    val description: String,
    val value: Double,
    val valueErrorMessage: String,
    val paymentType: PaymentType,
    val category: Category,
    val paid: Boolean,
    val dueDate: Long,
    val expired: Boolean
) {
    fun isValid() {}
}
