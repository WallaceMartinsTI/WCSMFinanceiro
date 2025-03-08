package com.wcsm.wcsmfinanceiro.domain.usecase.plus.subscriptions

import com.wcsm.wcsmfinanceiro.data.local.entity.Subscription
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateSubscriptionUseCase @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository
) {
    suspend operator fun invoke(subscription: Subscription): Flow<Response<Int>> {
        return subscriptionRepository.updateSubscription(subscription)
    }
}