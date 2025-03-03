package com.wcsm.wcsmfinanceiro.di.remote

import com.wcsm.wcsmfinanceiro.BuildConfig
import com.wcsm.wcsmfinanceiro.data.remote.api.ExchangeRateAPI
import com.wcsm.wcsmfinanceiro.data.remote.api.repository.CurrencyExchangeRepositoryImpl
import com.wcsm.wcsmfinanceiro.domain.repository.CurrencyExchangeRepository
import com.wcsm.wcsmfinanceiro.domain.usecase.plus.GetConvertedCurrencyUseCase
import com.wcsm.wcsmfinanceiro.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl("${Constants.EXCHANGE_RATE_API_BASE_URL}${BuildConfig.EXCHANGE_RATE_API_KEY}/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideExchangeRateAPI(
        retrofit: Retrofit
    ) : ExchangeRateAPI {
        return retrofit.create(ExchangeRateAPI::class.java)
    }

    @Provides
    fun provideCurrencyExchangeRepository(
        exchangeRateAPI: ExchangeRateAPI
    ) : CurrencyExchangeRepository {
        return CurrencyExchangeRepositoryImpl(exchangeRateAPI)
    }

    @Provides
    fun provideGetConvertedCurrencyUseCase(
        currencyExchangeRepository: CurrencyExchangeRepository
    ) : GetConvertedCurrencyUseCase {
        return GetConvertedCurrencyUseCase(currencyExchangeRepository)
    }
}