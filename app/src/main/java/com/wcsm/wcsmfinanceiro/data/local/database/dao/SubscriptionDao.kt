package com.wcsm.wcsmfinanceiro.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wcsm.wcsmfinanceiro.data.local.entity.Subscription
import kotlinx.coroutines.flow.Flow

@Dao
interface SubscriptionDao {
    @Insert
    fun saveSubscription(subscription: Subscription): Long

    @Update
    fun updateSubscription(subscription: Subscription): Int

    @Delete
    fun deleteSubscription(subscription: Subscription): Int

    @Query("SELECT * FROM subscriptions")
    fun selectAllSubscriptions(): Flow<List<Subscription>>
}