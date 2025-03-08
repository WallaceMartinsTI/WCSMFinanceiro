package com.wcsm.wcsmfinanceiro.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "subscriptions"
)
data class Subscription(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "subscription_id")
    val subscriptionId: Long,
    val title: String,
    @ColumnInfo(name = "start_date")
    val startDate: Long,
    @ColumnInfo(name = "due_date")
    val dueDate: Long,
    val price: Double,
    @ColumnInfo(name = "durationInMonths")
    val durationInMonths: Int,
    val expired: Boolean,
    @ColumnInfo(name = "automatic_renewal")
    val automaticRenewal: Boolean
)
