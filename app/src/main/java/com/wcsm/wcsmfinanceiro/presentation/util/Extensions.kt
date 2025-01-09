package com.wcsm.wcsmfinanceiro.presentation.util

import android.icu.util.Calendar
import com.wcsm.wcsmfinanceiro.domain.model.PaymentType
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

fun Long.formatDateInMillisToBrazillianDate(extendedYear: Boolean = true) : String {
    val pattern = if(extendedYear) "dd/MM/yyyy" else "dd/MM/yy"
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    return dateFormat.format(calendar.timeInMillis)
}

fun Double.toBrazilianReal() : String {
    val portugueseBrazilianLocale = Locale("pt", "BR")
    return NumberFormat.getCurrencyInstance(portugueseBrazilianLocale).format(this)
}