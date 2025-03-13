package com.wcsm.wcsmfinanceiro.domain.usecase.plus.subscriptions

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.wcsm.wcsmfinanceiro.data.local.entity.Subscription
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class UpdateSubscriptionUseCaseTest {

    @Mock
    private lateinit var subscriptionRepository: SubscriptionRepository

    private lateinit var updateSubscriptionUseCase: UpdateSubscriptionUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        updateSubscriptionUseCase = UpdateSubscriptionUseCase(subscriptionRepository)
    }

    @Test
    fun updateSubscriptionUseCase_updateSubscriptionUseCaseWithSuccess_shouldEmitSuccessResponse() = runTest {
        // GIVEN: A subscription to be updated
        val subscription = Subscription(
            subscriptionId = 1,
            title = "Netflix",
            startDate = 1737504000000,
            dueDate = 1737804000000,
            price = 79.99,
            durationInMonths = 3,
            expired = false,
            automaticRenewal = true
        )

        Mockito.`when`(subscriptionRepository.updateSubscription(subscription)).thenReturn(
            flow {
                emit(Response.Loading)
                emit(Response.Success(1))
            }
        )

        // WHEN: Trying to update the subscription
        updateSubscriptionUseCase(subscription).test {
            // THEN: Use case should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit a success response
            val rowsAffected = (awaitItem() as Response.Success).data
            assertThat(rowsAffected).isEqualTo(1)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateSubscriptionUseCase_updateSubscriptionUseCaseNoRowsAffected_shouldEmitErrorResponse() = runTest {
        // GIVEN: A subscription to be updated
        val subscription = Subscription(
            subscriptionId = 1,
            title = "Netflix",
            startDate = 1737504000000,
            dueDate = 1737804000000,
            price = 79.99,
            durationInMonths = 3,
            expired = false,
            automaticRenewal = true
        )
        val errorMessage = "Erro ao atualizar assinatura."

        Mockito.`when`(subscriptionRepository.updateSubscription(subscription)).thenReturn(
            flow {
                emit(Response.Loading)
                emit(Response.Error(errorMessage))
            }
        )

        // WHEN: Trying to update the subscription
        updateSubscriptionUseCase(subscription).test {
            // THEN: Use case should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit an error response
            assertThat((awaitItem() as Response.Error).message).isEqualTo(errorMessage)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateSubscriptionUseCase_updateSubscriptionUseCaseInvalidValue_shouldEmitErrorResponse() = runTest {
        // GIVEN: A subscription to be updated
        val subscription = Subscription(
            subscriptionId = 1,
            title = "Netflix",
            startDate = 1737504000000,
            dueDate = 1737804000000,
            price = 99999999.99,
            durationInMonths = 3,
            expired = false,
            automaticRenewal = true
        )

        // WHEN: Trying to update the subscription
        updateSubscriptionUseCase(subscription).test {
            // THEN: It should emit an error response
            assertThat((awaitItem() as Response.Error).message).isEqualTo("Valor muito alto (max. R$9.999,99).")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }
}