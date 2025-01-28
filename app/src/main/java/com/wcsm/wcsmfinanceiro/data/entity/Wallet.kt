package com.wcsm.wcsmfinanceiro.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "wallets"
)
data class Wallet(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "wallet_id")
    val walletId: Long,
    val title: String,
    val balance: Double,
)