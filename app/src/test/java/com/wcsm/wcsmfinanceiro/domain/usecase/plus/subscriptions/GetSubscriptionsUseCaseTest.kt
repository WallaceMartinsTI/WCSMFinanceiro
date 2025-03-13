package com.wcsm.wcsmfinanceiro.domain.usecase.plus.subscriptions

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.wcsm.wcsmfinanceiro.data.local.entity.Subscription
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetSubscriptionsUseCaseTest {

    @Mock
    private lateinit var subscriptionRepository: SubscriptionRepository

    private lateinit var getSubscriptionsUseCase: GetSubscriptionsUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        getSubscriptionsUseCase = GetSubscriptionsUseCase(subscriptionRepository)
    }

    @Test
    fun getSubscriptionsUseCase_getSubscriptionsUseCaseWithSuccess_shouldEmitSuccessResponse() = runTest {
        val expectedSubscriptions = listOf(
            Subscription(
                subscriptionId = 1,
                title = "Netflix",
                startDate = 1737504000000,
                dueDate = 1737804000000,
                price = 79.99,
                durationInMonths = 3,
                expired = false,
                automaticRenewal = true
            ),
            Subscription(
                subscriptionId = 2,
                title = "Amazon Music",
                startDate = 1737504000000,
                dueDate = 1737804000000,
                price = 79.99,
                durationInMonths = 3,
                expired = false,
                automaticRenewal = true
            )
        )

        Mockito.`when`(subscriptionRepository.getSubscriptions()).thenReturn(
            flow {
                emit(Response.Loading)
                emit(Response.Success(expectedSubscriptions))
            }
        )

        // WHEN: Trying to delete the subscription
        getSubscriptionsUseCase().test {
            // THEN: Use case should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit a success response
            assertThat((awaitItem() as Response.Success).data).isEqualTo(expectedSubscriptions)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getSubscriptionsUseCase_getSubscriptionsUseCaseNoSubscriptionsForTheInformedText_shouldEmitSuccessResponseWithAnEmptyList() = runTest {
        Mockito.`when`(subscriptionRepository.getSubscriptions()).thenReturn(
            flow {
                emit(Response.Loading)
                emit(Response.Success(emptyList()))
            }
        )

        // WHEN: Trying to delete the subscription
        getSubscriptionsUseCase().test {
            // THEN: Use case should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit a success response
            assertThat((awaitItem() as Response.Success).data).isEmpty()

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }
}