package com.wcsm.wcsmfinanceiro.data.local.repository

import android.database.sqlite.SQLiteConstraintException
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.wcsm.wcsmfinanceiro.data.local.database.dao.SubscriptionDao
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
class SubscriptionRepositoryImplTest {

    @Mock
    private lateinit var subscriptionDao: SubscriptionDao

    private lateinit var subscriptionRepository: SubscriptionRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        subscriptionRepository = SubscriptionRepositoryImpl(subscriptionDao)
    }

    @Test
    fun saveSubscription_saveSubscriptionWithSuccess_shouldEmitSuccessResponse() = runTest {
        // GIVEN: A subscription to be saved
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

        Mockito.`when`(subscriptionDao.saveSubscription(subscription)).thenReturn(subscription.subscriptionId)


        // WHEN: Trying to save the subscription
        subscriptionRepository.saveSubscription(subscription).test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit a success response
            assertThat((awaitItem() as Response.Success).data).isEqualTo(subscription.subscriptionId)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun saveSubscription_saveSubscriptionNoRowsAffected_shouldEmitErrorResponse() = runTest {
        // GIVEN: A subscription to be saved
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

        Mockito.`when`(subscriptionDao.saveSubscription(subscription)).thenReturn(0)

        // WHEN: Trying to save the subscription
        subscriptionRepository.saveSubscription(subscription).test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit an error response
            assertThat((awaitItem() as Response.Error).message).isEqualTo("Erro ao salvar assinatura.")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun saveSubscription_saveSubscriptionTwice_shouldEmitErrorResponse() = runTest {
        // GIVEN: A subscription to be saved
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

        Mockito.`when`(subscriptionDao.saveSubscription(subscription)).thenThrow(SQLiteConstraintException())

        // WHEN: Trying to save the subscription
        subscriptionRepository.saveSubscription(subscription).test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit an error response
            assertThat((awaitItem() as Response.Error).message).isEqualTo("Erro desconhecido ao salvar assinatura, informe o administrador.")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateSubscription_updateSubscriptionWithSuccess_shouldEmitSuccessResponse() = runTest {
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

        Mockito.`when`(subscriptionDao.updateSubscription(subscription)).thenReturn(1)


        // WHEN: Trying to update the subscription
        subscriptionRepository.updateSubscription(subscription).test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit a success response with rows affected
            val rowsAffected = (awaitItem() as Response.Success).data
            assertThat(rowsAffected).isEqualTo(1)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateSubscription_updateSubscriptionNoRowsAffected_shouldEmitErrorResponse() = runTest {
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

        Mockito.`when`(subscriptionDao.updateSubscription(subscription)).thenReturn(0)

        // WHEN: Trying to update the subscription
        subscriptionRepository.updateSubscription(subscription).test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit an error response
            assertThat((awaitItem() as Response.Error).message).isEqualTo("Erro ao atualizar assinatura.")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteSubscription_deleteSubscriptionWithSuccess_shouldEmitSuccessResponse() = runTest {
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

        Mockito.`when`(subscriptionDao.deleteSubscription(subscription)).thenReturn(1)

        // WHEN: Trying to update the subscription
        subscriptionRepository.deleteSubscription(subscription).test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit a success response
            assertThat((awaitItem() as Response.Success).data).isEqualTo(1)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteSubscription_deleteSubscriptionNoRowsAffected_shouldEmitErrorResponse() = runTest {
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

        Mockito.`when`(subscriptionDao.deleteSubscription(subscription)).thenReturn(0)

        // WHEN: Trying to update the subscription
        subscriptionRepository.deleteSubscription(subscription).test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit an error response
            assertThat((awaitItem() as Response.Error).message).isEqualTo("Erro ao deletar assinatura.")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getSubscriptions_getSubscriptionsWithSuccess_shouldEmitSuccessResponse() = runTest {
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

        Mockito.`when`(subscriptionDao.selectAllSubscriptions()).thenReturn(
            flow {
                emit(expectedSubscriptions)
            }
        )

        // GIVEN & WHEN: A request to select all subscriptions
        subscriptionRepository.getSubscriptions().test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit a success response with the subscriptions list
            assertThat((awaitItem() as Response.Success).data).isEqualTo(expectedSubscriptions)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getSubscriptions_getSubscriptionsWithFailure_shouldEmitErrorResponse() = runTest {
        Mockito.`when`(subscriptionDao.selectAllSubscriptions()).thenReturn(
            flow {
                throw RuntimeException("Erro ao acessar banco de dados.")
            }
        )

        // GIVEN & WHEN: A request to select all subscriptions
        subscriptionRepository.getSubscriptions().test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit an error response
            assertThat((awaitItem() as Response.Error).message).isEqualTo("Erro desconhecido ao buscar assinaturas, informe o administrador.")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }
}