package com.wcsm.wcsmfinanceiro.presentation.util

import android.icu.util.Calendar
import com.wcsm.wcsmfinanceiro.data.entity.Bill
import com.wcsm.wcsmfinanceiro.presentation.model.BillModalState
import java.text.Normalizer
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun Double.toBrazilianReal() : String {
    val portugueseBrazilianLocale = Locale("pt", "BR")
    return NumberFormat.getCurrencyInstance(portugueseBrazilianLocale).format(this)
}

fun Long.toBrazilianDateString(extendedYear: Boolean = true) : String {
    val pattern = if(extendedYear) "dd/MM/yyyy" else "dd/MM/yy"
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    val dateFormat = SimpleDateFormat(
        pattern,
        Locale("pt", "BR")
    ).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    return dateFormat.format(calendar.timeInMillis)
}

fun String.normalize() : String {
    return Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
}

fun BillModalState.toBill() : Bill {
    return Bill(
        id = this.id,
        billType = this.billType,
        origin = this.origin,
        title = this.title,
        value = this.value,
        description = this.description,
        date = this.date,
        paymentType = this.paymentType,
        paid = this.paid,
        dueDate = this.dueDate,
        expired = this.expired,
        category = this.category,
        tags = this.tags
    )
}