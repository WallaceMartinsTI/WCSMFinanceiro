package com.wcsm.wcsmfinanceiro.presentation.ui.view.plus.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.wcsm.wcsmfinanceiro.data.local.entity.Subscription
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.usecase.plus.subscriptions.DeleteSubscriptionUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.plus.subscriptions.GetSubscriptionsUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.plus.subscriptions.SaveSubscriptionUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.plus.subscriptions.UpdateSubscriptionUseCase
import com.wcsm.wcsmfinanceiro.presentation.model.CrudOperationType
import com.wcsm.wcsmfinanceiro.presentation.model.UiState
import com.wcsm.wcsmfinanceiro.presentation.model.plus.SubscriptionState
import com.wcsm.wcsmfinanceiro.util.toSubscription
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
class SubscriptionViewModelTest {

    @Mock
    private lateinit var getSubscriptionsUseCase: GetSubscriptionsUseCase

    @Mock
    private lateinit var saveSubscriptionsUseCase: SaveSubscriptionUseCase

    @Mock
    private lateinit var updateSubscriptionUseCase: UpdateSubscriptionUseCase

    @Mock
    private lateinit var deleteSubscriptionsUseCase: DeleteSubscriptionUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun updateUiState_updateUiState_shouldUpdateUiStateCorrectly() = runTest {
        val subscriptionViewModel = SubscriptionViewModel(
            getSubscriptionsUseCase, saveSubscriptionsUseCase,
            updateSubscriptionUseCase, deleteSubscriptionsUseCase
        )

        // GIVEN: A new uiState to be updated
        val uiState = UiState<CrudOperationType>()

        subscriptionViewModel.uiState.test {
            // At first uiState should start with an empty UiState
            assertThat(awaitItem()).isEqualTo(uiState)

            // WHEN: Passed a new uiState to be updated
            subscriptionViewModel.updateUiState(uiState.copy(isLoading = true))

            // THEN: uiState should match with updated uiState
            assertThat(awaitItem().isLoading).isTrue()

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateSubscriptionState_updateSubscriptionState_shouldUpdateCorrectly() = runTest {
        val subscriptionViewModel = SubscriptionViewModel(
            getSubscriptionsUseCase, saveSubscriptionsUseCase,
            updateSubscriptionUseCase, deleteSubscriptionsUseCase
        )

        // GIVEN: A new subscriptionState to be updated
        val subscriptionState = SubscriptionState()

        subscriptionViewModel.subscriptionStateFlow.test {
            // At first subscriptionStateFlow state should start with an empty SubscriptionState
            assertThat(awaitItem()).isEqualTo(subscriptionState)

            // WHEN: Passed a new subscription state to be updated
            subscriptionViewModel.updateSubscriptionState(subscriptionState.copy(title = "Título Atualizado"))

            // THEN: Subscription state should match with updated SubscriptionState
            assertThat(awaitItem().title).isEqualTo("Título Atualizado")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun resetSubscriptionState_resetSubscriptionState_shouldMatchWithAndEmptySubscriptionState() = runTest {
        val subscriptionViewModel = SubscriptionViewModel(
            getSubscriptionsUseCase, saveSubscriptionsUseCase,
            updateSubscriptionUseCase, deleteSubscriptionsUseCase
        )

        // GIVEN: A new subscriptionState to be updated
        val subscriptionState = SubscriptionState()

        subscriptionViewModel.subscriptionStateFlow.test {
            // At first subscriptionStateFlow state should start with an empty SubscriptionState
            assertThat(awaitItem()).isEqualTo(subscriptionState)

            // Update a subscription state to be reset
            subscriptionViewModel.updateSubscriptionState(subscriptionState.copy(title = "SubscriptionState Atualizado"))

            // Check if subscription state was updated
            assertThat(awaitItem().title).isEqualTo("SubscriptionState Atualizado")

            // WHEN: Reset subscription state
            subscriptionViewModel.resetSubscriptionState()

            // THEN: Subscription state should match with a reset subscriptionState
            assertThat(awaitItem()).isEqualTo(subscriptionState)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun resetUiState_resetUiState_shouldMatchWithAnEmptyUiState() = runTest {
        val subscriptionViewModel = SubscriptionViewModel(
            getSubscriptionsUseCase, saveSubscriptionsUseCase,
            updateSubscriptionUseCase, deleteSubscriptionsUseCase
        )

        // GIVEN: A reset ui state
        val uiState = UiState<CrudOperationType>()

        subscriptionViewModel.uiState.test {
            // At first uiState state should start with an empty UiState
            assertThat(awaitItem()).isEqualTo(uiState)

            // Update uiState to be reset
            subscriptionViewModel.updateUiState(uiState.copy(error = "Erro simulado"))

            assertThat(awaitItem().error).isEqualTo("Erro simulado")

            // WHEN: Reset uiState
            subscriptionViewModel.resetUiState()

            // THEN: uiState should match with a reset uiState
            assertThat(awaitItem()).isEqualTo(uiState)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun resetSubscriptionStateErrorMessages_resetSubscriptionStateErrorMessages_shouldResetAllErrorMessagesFromSubscriptionState() = runTest {
        val subscriptionViewModel = SubscriptionViewModel(
            getSubscriptionsUseCase, saveSubscriptionsUseCase,
            updateSubscriptionUseCase, deleteSubscriptionsUseCase
        )

        // GIVEN: A subscription state with error values
        val subscriptionStateWithErrors = SubscriptionState(
            titleErrorMessage = "Erro no Título",
            startDateErrorMessage = "Erro na Data Inicial",
            dueDateErrorMessage = "Erro na Data Final",
            priceErrorMessage = "Erro no Preço",
            durationInMonthsErrorMessage = "Erro na Duração",
            responseErrorMessage = "Erro na Resposta da Requisição"
        )

        subscriptionViewModel.updateSubscriptionState(subscriptionStateWithErrors)

        subscriptionViewModel.subscriptionStateFlow.test {
            assertThat(awaitItem()).isEqualTo(subscriptionStateWithErrors)

            // WHEN: Reset error messages
            subscriptionViewModel.resetSubscriptionStateErrorMessage()

            // THEN: SubscriptionState should have empty error message
            val subscriptionStateAfterReset = awaitItem()

            assertThat(subscriptionStateAfterReset.titleErrorMessage).isEmpty()
            assertThat(subscriptionStateAfterReset.startDateErrorMessage).isEmpty()
            assertThat(subscriptionStateAfterReset.dueDateErrorMessage).isEmpty()
            assertThat(subscriptionStateAfterReset.priceErrorMessage).isEmpty()
            assertThat(subscriptionStateAfterReset.durationInMonthsErrorMessage).isEmpty()
            assertThat(subscriptionStateAfterReset.responseErrorMessage).isEmpty()

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getSubscriptions_getSubscriptions_shouldFillSubscriptionsList() = runTest {
        val subscriptions = listOf(
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
            ),
            Subscription(
                subscriptionId = 3,
                title = "Disney Plus",
                startDate = 1737504000000,
                dueDate = 1737804000000,
                price = 79.99,
                durationInMonths = 3,
                expired = false,
                automaticRenewal = true
            )
        )

        val subscriptionViewModel = SubscriptionViewModel(
            getSubscriptionsUseCase, saveSubscriptionsUseCase,
            updateSubscriptionUseCase, deleteSubscriptionsUseCase
        )

        Mockito.`when`(getSubscriptionsUseCase()).thenReturn(
            flow { emit(Response.Success(subscriptions)) }
        )

        subscriptionViewModel.subscriptions.test {
            assertThat(awaitItem()).isNull()

            // GIVEN & WHEN: List of subscriptions is requested
            subscriptionViewModel.getSubscriptions()

            // THEN: subscriptions list should be filled with requested subscriptions and reversed
            assertThat(awaitItem()).isEqualTo(subscriptions.reversed())

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun saveSubscription_saveSubscriptionToUseCase_shouldSaveSubscriptionAndFillUiStateWithSuccess() = runTest {
        // GIVEN: A subscription to be saved
        val subscriptionToBeSaved = SubscriptionState(
            subscriptionId = 1,
            title = "Netflix",
            startDate = 1737504000000,
            dueDate = 1737804000000,
            price = 79.99,
            durationInMonths = 3,
            expired = false,
            automaticRenewal = true
        )

        val subscriptionViewModel = SubscriptionViewModel(
            getSubscriptionsUseCase, saveSubscriptionsUseCase,
            updateSubscriptionUseCase, deleteSubscriptionsUseCase
        )

        Mockito.`when`(saveSubscriptionsUseCase(subscriptionToBeSaved.toSubscription())).thenReturn(
            flow { emit(Response.Success(1L)) }
        )
        Mockito.`when`(getSubscriptionsUseCase()).thenReturn(
            flow { emit(Response.Success(emptyList())) }
        )

        subscriptionViewModel.updateSubscriptionState(subscriptionToBeSaved)

        subscriptionViewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(UiState<CrudOperationType>())

            // WHEN: Save the subscription
            subscriptionViewModel.saveSubscription(subscriptionToBeSaved)

            assertThat(awaitItem().operationType).isEqualTo(CrudOperationType.SAVE)

            // THEN: Should emit success
            assertThat(awaitItem().success).isTrue()

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateSubscription_updateSubscriptionToUseCase_shouldUpdateSubscriptionAndFillUiStateWithSuccess() = runTest {
        // GIVEN: A subscription to be saved
        val subscriptionToBeSaved = SubscriptionState(
            subscriptionId = 1,
            title = "Netflix",
            startDate = 1737504000000,
            dueDate = 1737804000000,
            price = 79.99,
            durationInMonths = 3,
            expired = false,
            automaticRenewal = true
        )

        val subscriptionViewModel = SubscriptionViewModel(
            getSubscriptionsUseCase, saveSubscriptionsUseCase,
            updateSubscriptionUseCase, deleteSubscriptionsUseCase
        )

        Mockito.`when`(updateSubscriptionUseCase(subscriptionToBeSaved.toSubscription())).thenReturn(
            flow { emit(Response.Success(1)) }
        )
        Mockito.`when`(getSubscriptionsUseCase()).thenReturn(
            flow { emit(Response.Success(emptyList())) }
        )

        subscriptionViewModel.updateSubscriptionState(subscriptionToBeSaved)

        subscriptionViewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(UiState<CrudOperationType>())

            // WHEN: Update the subscription
            subscriptionViewModel.updateSubscription(subscriptionToBeSaved)

            assertThat(awaitItem().operationType).isEqualTo(CrudOperationType.UPDATE)

            // THEN: Should emit success
            assertThat(awaitItem().success).isTrue()

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteSubscription_deleteSubscriptionToUseCase_shouldSaveSubscriptionAndFillUiStateWithSuccess() = runTest {
        // GIVEN: A subscription to be saved
        val subscriptionToBeSaved = SubscriptionState(
            subscriptionId = 1,
            title = "Netflix",
            startDate = 1737504000000,
            dueDate = 1737804000000,
            price = 79.99,
            durationInMonths = 3,
            expired = false,
            automaticRenewal = true
        )

        val subscriptionViewModel = SubscriptionViewModel(
            getSubscriptionsUseCase, saveSubscriptionsUseCase,
            updateSubscriptionUseCase, deleteSubscriptionsUseCase
        )

        Mockito.`when`(deleteSubscriptionsUseCase(subscriptionToBeSaved.toSubscription())).thenReturn(
            flow { emit(Response.Success(1)) }
        )
        Mockito.`when`(getSubscriptionsUseCase()).thenReturn(
            flow { emit(Response.Success(emptyList())) }
        )

        subscriptionViewModel.updateSubscriptionState(subscriptionToBeSaved)

        subscriptionViewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(UiState<CrudOperationType>())

            // WHEN: Delete the subscription
            subscriptionViewModel.deleteSubscription(subscriptionToBeSaved)

            assertThat(awaitItem().operationType).isEqualTo(CrudOperationType.DELETE)

            // THEN: Should emit success
            assertThat(awaitItem().success).isTrue()

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }
}