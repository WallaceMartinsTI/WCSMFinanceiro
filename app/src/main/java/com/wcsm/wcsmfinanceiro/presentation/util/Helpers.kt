package com.wcsm.wcsmfinanceiro.presentation.util

import android.icu.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale

fun formatDateInMillisToBrazillianDate(dateInMillis: Long) : String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = dateInMillis
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(calendar.timeInMillis)
}