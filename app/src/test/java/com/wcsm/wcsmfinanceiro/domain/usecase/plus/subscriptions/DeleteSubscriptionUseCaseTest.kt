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
class DeleteSubscriptionUseCaseTest {

    @Mock
    private lateinit var subscriptionRepository: SubscriptionRepository

    private lateinit var deleteSubscriptionUseCase: DeleteSubscriptionUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        deleteSubscriptionUseCase = DeleteSubscriptionUseCase(subscriptionRepository)
    }

    @Test
    fun deleteSubscriptionUseCase_deleteSubscriptionUseCaseWithSuccess_shouldEmitSuccessResponse() = runTest {
        // GIVEN: A subscription to be deleted
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

        Mockito.`when`(subscriptionRepository.deleteSubscription(subscription)).thenReturn(
            flow {
                emit(Response.Loading)
                emit(Response.Success(1))
            }
        )

        // WHEN: Trying to delete the subscription
        deleteSubscriptionUseCase(subscription).test {
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
    fun deleteSubscriptionUseCase_deleteSubscriptionUseCaseNoRowsAffected_shouldEmitErrorResponse() = runTest {
        // GIVEN: A subscription to be deleted
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
        val errorMessage = "Erro ao deletar assinatura."

        Mockito.`when`(subscriptionRepository.deleteSubscription(subscription)).thenReturn(
            flow {
                emit(Response.Loading)
                emit(Response.Error(errorMessage))
            }
        )

        // WHEN: Trying to delete the subscription
        deleteSubscriptionUseCase(subscription).test {
            // THEN: Use case should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit an error response
            assertThat((awaitItem() as Response.Error).message).isEqualTo(errorMessage)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }
}