package com.wcsm.wcsmfinanceiro.domain.repository

import com.wcsm.wcsmfinanceiro.domain.model.Response
import kotlinx.coroutines.flow.Flow

interface CurrencyExchangeRepository {
    suspend fun convertCurrency(
        baseCode: String,
        targetCode: String,
        value: Double
    ) : Flow<Response<Pair<Double, Double>>>
}