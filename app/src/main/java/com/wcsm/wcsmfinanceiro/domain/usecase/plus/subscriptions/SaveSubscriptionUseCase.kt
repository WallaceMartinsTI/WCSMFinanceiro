package com.wcsm.wcsmfinanceiro.domain.usecase.plus.subscriptions

import com.wcsm.wcsmfinanceiro.data.local.entity.Subscription
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveSubscriptionUseCase @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository
) {
    suspend operator fun invoke(subscription: Subscription): Flow<Response<Long>> {
        if(subscription.price > 9999.99) {
            return flow {
                emit(Response.Error("Valor muito alto (max. R$9.999,99)."))
            }
        }

        return subscriptionRepository.saveSubscription(subscription)
    }
}