package com.wcsm.wcsmfinanceiro.presentation.model

import android.util.Log
import com.wcsm.wcsmfinanceiro.data.entity.BillType
import com.wcsm.wcsmfinanceiro.data.entity.Category
import com.wcsm.wcsmfinanceiro.data.entity.PaymentType

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
        Log.i("#-# TESTE #-#", "RESETOU AS MENSAGENS DE ERRO")
        titleErrorMessage = ""
        dateErrorMessage = ""
        valueErrorMessage = ""
    }
}
