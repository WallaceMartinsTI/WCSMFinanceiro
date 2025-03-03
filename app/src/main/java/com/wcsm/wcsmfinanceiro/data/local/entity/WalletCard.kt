package com.wcsm.wcsmfinanceiro.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "wallet_cards",
    foreignKeys = [
        ForeignKey(
            entity = Wallet::class,
            parentColumns = ["wallet_id"],
            childColumns = ["wallet_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class WalletCard(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "wallet_card_id")
    val walletCardId: Long,
    @ColumnInfo(name = "wallet_id")
    val walletId: Long,
    val title: String,
    val limit: Double,
    val spent: Double,
    val available: Double,
    val blocked: Boolean
)
