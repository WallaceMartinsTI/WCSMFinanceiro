package com.wcsm.wcsmfinanceiro.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wcsm.wcsmfinanceiro.data.local.model.BillType
import com.wcsm.wcsmfinanceiro.data.local.model.PaymentType

@Entity(
    tableName = "bills"
)
data class Bill(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bill_id")
    val billId: Long,
    @ColumnInfo(name = "wallet_id")
    val walletId: Long,
    @ColumnInfo(name = "wallet_card_id")
    val walletCardId: Long,
    @ColumnInfo(name = "bill_type")
    val billType: BillType,
    val origin: String,
    val title: String,
    val value: Double,
    val description: String,
    val date: Long,
    @ColumnInfo(name = "payment_type")
    val paymentType: PaymentType,
    val paid: Boolean,
    @ColumnInfo(name = "due_date")
    val dueDate: Long,
    val expired: Boolean,
    val category: String,
    val tags: List<String>
)