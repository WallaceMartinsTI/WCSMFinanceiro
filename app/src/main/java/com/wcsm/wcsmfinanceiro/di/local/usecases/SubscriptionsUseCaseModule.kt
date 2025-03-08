package com.wcsm.wcsmfinanceiro.di.local.usecases

import com.wcsm.wcsmfinanceiro.domain.repository.SubscriptionRepository
import com.wcsm.wcsmfinanceiro.domain.usecase.plus.subscriptions.DeleteSubscriptionUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.plus.subscriptions.GetSubscriptionsUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.plus.subscriptions.SaveSubscriptionUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.plus.subscriptions.UpdateSubscriptionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object SubscriptionsUseCaseModule {

    @Provides
    fun provideGetSubscriptionsUseCase(
        subscriptionRepository: SubscriptionRepository
    ): GetSubscriptionsUseCase {
        return GetSubscriptionsUseCase(subscriptionRepository)
    }

    @Provides
    fun provideSaveSubscriptionsUseCase(
        subscriptionRepository: SubscriptionRepository
    ): SaveSubscriptionUseCase {
        return SaveSubscriptionUseCase(subscriptionRepository)
    }

    @Provides
    fun provideUpdateSubscriptionsUseCase(
        subscriptionRepository: SubscriptionRepository
    ): UpdateSubscriptionUseCase {
        return UpdateSubscriptionUseCase(subscriptionRepository)
    }

    @Provides
    fun provideDeleteSubscriptionsUseCase(
        subscriptionRepository: SubscriptionRepository
    ): DeleteSubscriptionUseCase {
        return DeleteSubscriptionUseCase(subscriptionRepository)
    }
}