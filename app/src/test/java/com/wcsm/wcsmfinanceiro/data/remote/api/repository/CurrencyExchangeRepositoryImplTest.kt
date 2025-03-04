package com.wcsm.wcsmfinanceiro.data.remote.api.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.wcsm.wcsmfinanceiro.data.remote.api.ExchangeRateAPI
import com.wcsm.wcsmfinanceiro.data.remote.api.dto.ExchangeRateResponseDTO
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.repository.CurrencyExchangeRepository
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import okhttp3.MediaType
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
class CurrencyExchangeRepositoryImplTest {

    @Mock
    private lateinit var exchangeRateAPI: ExchangeRateAPI

    private lateinit var currencyExchangeRepository: CurrencyExchangeRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        currencyExchangeRepository = CurrencyExchangeRepositoryImpl(exchangeRateAPI)
    }

    @Test
    fun convertCurrency_convertCurrencyWithSuccess_shouldEmitSuccessResponse() = runTest {
        val expectedReturn = ExchangeRateResponseDTO(
            baseCode = "USD",
            conversionRate = 1.0,
            conversionResult = 5.8884,
            documentation = "https://www.exchangerate-api.com/docs",
            result = "success",
            targetCode = "BRL",
            termsOfUse = "https://www.exchangerate-api.com/terms",
            timeLastUpdateUnix = 1585267200,
            timeLastUpdateUtc = "Fri, 27 Mar 2020 00:00:00 +0000",
            timeNextUpdateUnix = 1585270800,
            timeNextUpdateUtc = "Sat, 28 Mar 2020 01:00:00 +0000"
        )

        Mockito.`when`(exchangeRateAPI.convertValue(anyString(), anyString(), anyDouble())).thenReturn(
            retrofit2.Response.success(expectedReturn)
        )

        // GIVEN: The values to request
        val baseCode = "USD"
        val targetCode = "BRL"
        val value = 1.0

        // WHEN: Convert the value
        currencyExchangeRepository.convertCurrency(baseCode, targetCode, value).test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit a success response
            assertThat(
                (awaitItem() as Response.Success).data
            ).isEqualTo(Pair(expectedReturn.conversionResult, value))

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun convertCurrency_convertCurrencyWithError_shouldEmitErrorResponseWithEmptyBodyMessage() = runTest {
        Mockito.`when`(exchangeRateAPI.convertValue(anyString(), anyString(), anyDouble())).thenReturn(
            retrofit2.Response.success(null)
        )

        val expectedErrorMessage = "Erro, requisição retornou vazio."

        // GIVEN: The values to request
        val baseCode = "USD"
        val targetCode = "BRL"
        val value = 1.0

        // WHEN: Convert the value
        currencyExchangeRepository.convertCurrency(baseCode, targetCode, value).test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit an error response
            assertThat((awaitItem() as Response.Error).message).isEqualTo(expectedErrorMessage)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun convertCurrency_convertCurrencyWithError_shouldEmitErrorResponseWithRequestFailedMessage() = runTest {
        Mockito.`when`(exchangeRateAPI.convertValue(anyString(), anyString(), anyDouble())).thenReturn(
            retrofit2.Response.error(
                500,
                ResponseBody.create(MediaType.get("application/json"), "Internal Server Error")
            )
        )

        val expectedErrorMessage = "Erro, a requisição falhou."

        // GIVEN: The values to request
        val baseCode = "USD"
        val targetCode = "BRL"
        val value = 1.0

        // WHEN: Convert the value
        currencyExchangeRepository.convertCurrency(baseCode, targetCode, value).test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit an error response
            assertThat((awaitItem() as Response.Error).message).isEqualTo(expectedErrorMessage)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }
}