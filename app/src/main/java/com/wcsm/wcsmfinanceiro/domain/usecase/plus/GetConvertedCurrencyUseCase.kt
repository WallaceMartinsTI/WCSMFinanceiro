package com.wcsm.wcsmfinanceiro.domain.usecase.plus

import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.repository.CurrencyExchangeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetConvertedCurrencyUseCase @Inject constructor(
    private val currencyExchangeRepository: CurrencyExchangeRepository
) {
    suspend operator fun invoke(
        baseCode: String,
        targetCode: String,
        value: Double
    ) : Flow<Response<Pair<Double, Double>>> {
        return currencyExchangeRepository.convertCurrency(baseCode, targetCode, value)
    }
}