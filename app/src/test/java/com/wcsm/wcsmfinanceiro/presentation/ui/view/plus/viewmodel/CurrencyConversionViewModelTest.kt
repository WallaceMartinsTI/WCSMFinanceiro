package com.wcsm.wcsmfinanceiro.presentation.ui.view.plus.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.usecase.plus.GetConvertedCurrencyUseCase
import com.wcsm.wcsmfinanceiro.presentation.model.UiState
import com.wcsm.wcsmfinanceiro.presentation.model.bills.BillState
import com.wcsm.wcsmfinanceiro.presentation.model.plus.CurrencyConversionState
import com.wcsm.wcsmfinanceiro.presentation.ui.view.bills.BillsViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyDouble
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CurrencyConversionViewModelTest {

    @Mock
    private lateinit var getConvertedCurrencyUseCase: GetConvertedCurrencyUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun updateCurrencyConversionStateFlow_updateCurrencyConversionStateFlow_shouldUpdateCorrectly() = runTest {
        val expectedCurrencyConversionState = CurrencyConversionState(
            baseCode = "USD",
            targetCode = "BRL",
            valueToConvert = 1.0
        )

        val currencyConversionViewModel = CurrencyConversionViewModel(getConvertedCurrencyUseCase)

        // GIVEN: A currency conversion state to be updated
        val currencyConversionState = CurrencyConversionState()

        currencyConversionViewModel.currencyConversionStateFlow.test {
            assertThat(awaitItem()).isEqualTo(currencyConversionState)

            // WHEN: Update currency conversion state
            currencyConversionViewModel.updateCurrencyConversionStateFlow(
                currencyConversionState.copy(
                    baseCode = "USD",
                    targetCode = "BRL",
                    valueToConvert = 1.0
                )
            )

            // THEN: Currency conversion state should match with expected
            assertThat(awaitItem()).isEqualTo(expectedCurrencyConversionState)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateUiState_updateUiState_shouldUpdateUiStateCorrectly() = runTest {
        val currencyConversionViewModel = CurrencyConversionViewModel(getConvertedCurrencyUseCase)

        // GIVEN: A new uiState to be updated
        val uiState = UiState<Nothing>()

        currencyConversionViewModel.uiState.test {
            // At first uiState should start with an empty UiState
            assertThat(awaitItem()).isEqualTo(uiState)

            // WHEN: Passed a new uiState to be updated
            currencyConversionViewModel.updateUiState(uiState.copy(isLoading = true))

            // THEN: uiState should match with updated uiState
            assertThat(awaitItem().isLoading).isTrue()

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun resetCurrencyConversionState_resetCurrencyConversionState_shouldMatchWithAnEmptyCurrencyConversionState() = runTest {
        val currencyConversionViewModel = CurrencyConversionViewModel(getConvertedCurrencyUseCase)

        // GIVEN: A reset currency conversion state
        val currencyConversionState = CurrencyConversionState()

        currencyConversionViewModel.currencyConversionStateFlow.test {
            // At first currencyConversionStateFlow state should start with an empty CurrencyConversionState
            assertThat(awaitItem()).isEqualTo(currencyConversionState)

            // Update a currency conversion state to be reset
            currencyConversionViewModel.updateCurrencyConversionStateFlow(
                currencyConversionState.copy(
                    baseCode = "USD",
                    targetCode = "BRL",
                    valueToConvert = 1.0
                )
            )

            // Check if bill state was updated
            val updateCurrencyConversionState = awaitItem()
            assertThat(updateCurrencyConversionState.baseCode).isEqualTo("USD")
            assertThat(updateCurrencyConversionState.targetCode).isEqualTo("BRL")
            assertThat(updateCurrencyConversionState.valueToConvert).isEqualTo(1.0)

            // WHEN: Reset currency conversion state
            currencyConversionViewModel.resetCurrencyConversionStateFlow()

            // THEN: Currency conversion state should match with a reset currencyConversionState
            assertThat(awaitItem()).isEqualTo(currencyConversionState)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun resetUiState_resetUiState_shouldMatchWithAnEmptyUiState() = runTest {
        val currencyConversionViewModel = CurrencyConversionViewModel(getConvertedCurrencyUseCase)

        // GIVEN: A reset ui state
        val uiState = UiState<Nothing>()

        currencyConversionViewModel.uiState.test {
            // At first uiState state should start with an empty UiState
            assertThat(awaitItem()).isEqualTo(uiState)

            // Update uiState to be reset
            currencyConversionViewModel.updateUiState(uiState.copy(error = "Erro simulado"))

            assertThat(awaitItem().error).isEqualTo("Erro simulado")

            // WHEN: Reset uiState
            currencyConversionViewModel.resetUiState()

            // THEN: uiState should match with a reset uiState
            assertThat(awaitItem()).isEqualTo(uiState)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getConvertedCurrency_getConvertedCurrency_shouldFillCurrencyConversionStateConvertedValue() = runTest {
        val expectedValue = Pair(5.8884, 5.8884)

        val currencyConversionViewModel = CurrencyConversionViewModel(getConvertedCurrencyUseCase)

        Mockito.`when`(getConvertedCurrencyUseCase(anyString(), anyString(), anyDouble())).thenReturn(
            flow {
                emit(Response.Loading)
                emit(Response.Success(expectedValue))
            }
        )

        val initialCurrencyConversionState = CurrencyConversionState()

        currencyConversionViewModel.currencyConversionStateFlow.test {
            assertThat(awaitItem()).isEqualTo(initialCurrencyConversionState)

            // GIVEN: Currency conversion state filled with request data
            currencyConversionViewModel.updateCurrencyConversionStateFlow(
                initialCurrencyConversionState.copy(
                    baseCode = "USD",
                    targetCode = "BRL",
                    valueToConvert = 1.0
                )
            )

            val updatedInitialCurrencyConversion = awaitItem()
            assertThat(updatedInitialCurrencyConversion.baseCode).isEqualTo("USD")
            assertThat(updatedInitialCurrencyConversion.targetCode).isEqualTo("BRL")
            assertThat(updatedInitialCurrencyConversion.valueToConvert).isEqualTo(1.0)

            // WHEN: Get converted value
            currencyConversionViewModel.getConvertedCurrency(
                initialCurrencyConversionState.copy(
                    baseCode = initialCurrencyConversionState.baseCode,
                    targetCode = initialCurrencyConversionState.targetCode,
                    valueToConvert = initialCurrencyConversionState.valueToConvert
                )
            )

            // THEN: Converted value should match with expected
            assertThat(awaitItem().convertedValue).isEqualTo(expectedValue.first)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }
}