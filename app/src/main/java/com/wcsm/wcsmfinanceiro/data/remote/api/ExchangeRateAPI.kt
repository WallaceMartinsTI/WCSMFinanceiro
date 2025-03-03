package com.wcsm.wcsmfinanceiro.data.remote.api

import com.wcsm.wcsmfinanceiro.data.remote.api.dto.ExchangeRateResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateAPI {

    @GET("pair/{base}/{target}/{value}")
    suspend fun convertValue(
        @Path("base") base: String,
        @Path("target") target: String,
        @Path("value") value: Double
    ) : Response<ExchangeRateResponseDTO>

}