package com.wcsm.wcsmfinanceiro.domain.repository

import com.wcsm.wcsmfinanceiro.data.local.entity.Subscription
import com.wcsm.wcsmfinanceiro.domain.model.Response
import kotlinx.coroutines.flow.Flow

interface SubscriptionRepository {
    suspend fun saveSubscription(subscription: Subscription): Flow<Response<Long>>
    suspend fun updateSubscription(subscription: Subscription): Flow<Response<Int>>
    suspend fun deleteSubscription(subscription: Subscription): Flow<Response<Int>>
    suspend fun getSubscriptions(): Flow<Response<List<Subscription>>>
}