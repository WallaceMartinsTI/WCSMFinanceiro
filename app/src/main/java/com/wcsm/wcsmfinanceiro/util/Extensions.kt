package com.wcsm.wcsmfinanceiro.util

import android.icu.util.Calendar
import com.wcsm.wcsmfinanceiro.data.local.entity.Bill
import com.wcsm.wcsmfinanceiro.data.local.entity.Wallet
import com.wcsm.wcsmfinanceiro.data.local.entity.WalletCard
import com.wcsm.wcsmfinanceiro.presentation.model.bills.BillState
import com.wcsm.wcsmfinanceiro.presentation.model.wallet.WalletCardState
import com.wcsm.wcsmfinanceiro.presentation.model.wallet.WalletState
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

fun String.brazilianDateToTimeInMillis(extendedYear: Boolean = true) : Long? {
    val pattern = if(extendedYear) "dd/MM/yyyy" else "dd/MM/yy"
    val dateFormat = SimpleDateFormat(pattern, Locale("pt", "BR")).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    val date = dateFormat.parse(this)
    return date?.time
}

fun Bill.toBillState() : BillState {
    return BillState(
        billId = this.billId,
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

fun BillState.toBill() : Bill {
    return Bill(
        billId = this.billId,
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

fun Wallet.toWalletState() : WalletState {
    return WalletState(
        walletId = this.walletId,
        title = this.title,
        balance = this.balance
    )
}

fun WalletState.toWallet() : Wallet {
    return Wallet(
        walletId = this.walletId,
        title = this.title,
        balance = this.balance
    )
}

fun WalletCard.toWalletCardState() : WalletCardState {
    return WalletCardState(
        walletId = this.walletId,
        walletCardId = this.walletCardId,
        title = this.title,
        limit = this.limit,
        spent = this.spent,
        available = this.available,
        blocked = this.blocked
    )
}

fun WalletCardState.toWalletCard() : WalletCard {
    return WalletCard(
        walletId = this.walletId,
        walletCardId = this.walletCardId,
        title = this.title,
        limit = this.limit,
        spent = this.spent,
        available = this.available,
        blocked = this.blocked
    )
}