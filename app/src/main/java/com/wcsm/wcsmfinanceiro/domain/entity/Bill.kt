package com.wcsm.wcsmfinanceiro.domain.entity

data class Bill(
    val id: Long,
    val billType: BillType,
    val origin: String?,
    val title: String,
    val value: Double,
    val description: String?,
    val date: Long,
    val paymentType: PaymentType?,
    val paid: Boolean?,
    val dueDate: Long?,
    val expired: Boolean?,
    val category: Category?,
    val tags: List<String>?
)
