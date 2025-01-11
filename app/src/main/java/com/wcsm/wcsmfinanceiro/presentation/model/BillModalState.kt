package com.wcsm.wcsmfinanceiro.presentation.model

import com.wcsm.wcsmfinanceiro.domain.model.BillType
import com.wcsm.wcsmfinanceiro.domain.model.Category
import com.wcsm.wcsmfinanceiro.domain.model.PaymentType

data class BillModalState(
    val id: Long,
    val billType: BillType,
    val origin: String,
    val title: String,
    var titleErrorMessage: String,
    val date: Long,
    var dateErrorMessage: String,
    val description: String,
    val value: Double,
    var valueErrorMessage: String,
    val paymentType: PaymentType,
    val category: Category,
    val paid: Boolean,
    val dueDate: Long,
    val expired: Boolean,
    val tags: List<String>
) {
    fun resetErrorMessages() {
        titleErrorMessage = ""
        dateErrorMessage = ""
        valueErrorMessage = ""
    }
}
