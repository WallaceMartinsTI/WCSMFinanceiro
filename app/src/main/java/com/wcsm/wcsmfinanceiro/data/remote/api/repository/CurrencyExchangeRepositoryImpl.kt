package com.wcsm.wcsmfinanceiro.data.remote.api.repository

import com.wcsm.wcsmfinanceiro.data.remote.api.ExchangeRateAPI
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.repository.CurrencyExchangeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CurrencyExchangeRepositoryImpl @Inject constructor(
    private val exchangeRateAPI: ExchangeRateAPI
) : CurrencyExchangeRepository {
    override suspend fun convertCurrency(
        baseCode: String,
        targetCode: String,
        value: Double,
    ): Flow<Response<Pair<Double, Double>>> = flow {
        try {
            emit(Response.Loading)

            val exchangeResult = exchangeRateAPI.convertValue(baseCode, targetCode, value)

            if(exchangeResult.isSuccessful) {
                val resultBody = exchangeResult.body()
                if(resultBody != null) {
                    val result = Pair(resultBody.conversionResult, resultBody.conversionRate)
                    emit(Response.Success(result))
                } else {
                    emit(Response.Error("Erro, requisição retornou vazio."))
                }
            } else {
                emit(Response.Error("Erro, a requisição falhou."))
            }

        } catch (e: Exception) {
            e.printStackTrace()
            emit(Response.Error("Erro desconhecido, tente novamente mais tarde."))
        }
    }
}