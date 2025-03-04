package com.wcsm.wcsmfinanceiro.domain.usecase.plus

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.repository.CurrencyExchangeRepository
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
class GetConvertedCurrencyUseCaseTest {

    @Mock
    private lateinit var currencyExchangeRepository: CurrencyExchangeRepository

    private lateinit var getConvertedCurrencyUseCase: GetConvertedCurrencyUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        getConvertedCurrencyUseCase = GetConvertedCurrencyUseCase(currencyExchangeRepository)
    }

    @Test
    fun getConvertedCurrencyUseCase_getConvertedCurrencyFromRepository_shouldEmitSuccess() = runTest {
        val expectedResponse = Pair(5.8876, 5.8876)

        Mockito.`when`(currencyExchangeRepository.convertCurrency(anyString(), anyString(), anyDouble())).thenReturn(
            flow {
                emit(Response.Loading)
                emit(Response.Success(expectedResponse))
            }
        )

        val baseCode = "USD"
        val targetCode = "BRL"
        val value = 1.0

        // GIVEN & WHEN: Request current conversion to repository
        getConvertedCurrencyUseCase(baseCode, targetCode, value).test {
            // THEN: Use case should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit a success response
            assertThat((awaitItem() as Response.Success).data).isEqualTo(expectedResponse)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getConvertedCurrencyUseCase_getConvertedCurrencyFromRepository_shouldEmitErrorWithEmptyBodyMessage() = runTest {
        val expectedErrorMessage = "Erro, requisição retornou vazio."

        Mockito.`when`(currencyExchangeRepository.convertCurrency(anyString(), anyString(), anyDouble())).thenReturn(
            flow {
                emit(Response.Loading)
                emit(Response.Error(expectedErrorMessage))
            }
        )

        val baseCode = "USD"
        val targetCode = "BRL"
        val value = 1.0

        // GIVEN & WHEN: Request current conversion to repository
        getConvertedCurrencyUseCase(baseCode, targetCode, value).test {
            // THEN: Use case should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit an error response
            assertThat((awaitItem() as Response.Error).message).isEqualTo(expectedErrorMessage)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getConvertedCurrencyUseCase_getConvertedCurrencyFromRepository_shouldEmitErrorWithRequestFailedMessage() = runTest {
        val expectedErrorMessage = "Erro, a requisição falhou."

        Mockito.`when`(currencyExchangeRepository.convertCurrency(anyString(), anyString(), anyDouble())).thenReturn(
            flow {
                emit(Response.Loading)
                emit(Response.Error(expectedErrorMessage))
            }
        )

        val baseCode = "USD"
        val targetCode = "BRL"
        val value = 1.0

        // GIVEN & WHEN: Request current conversion to repository
        getConvertedCurrencyUseCase(baseCode, targetCode, value).test {
            // THEN: Use case should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit an error response
            assertThat((awaitItem() as Response.Error).message).isEqualTo(expectedErrorMessage)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }
}