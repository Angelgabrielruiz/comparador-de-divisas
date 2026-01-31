package com.angelruiz.convertidor_divisas.core.network

import com.angelruiz.convertidor_divisas.features.currency.data.datasources.remote.model.CurrencyResponse
import com.angelruiz.convertidor_divisas.features.currency.data.datasources.remote.model.HistoryResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CurrencyApi {

    // Obtener tasas actuales
    // Ejemplo: https://api.frankfurter.app/latest?from=USD
    @GET("latest")
    suspend fun getLatestRates(
        @Query("from") base: String
    ): CurrencyResponse

    // Obtener historial (Time Series)
    // Ejemplo: https://api.frankfurter.app/2020-01-01..2020-01-31?from=USD&to=MXN
    @GET("{startDate}..{endDate}")
    suspend fun getHistory(
        @Path("startDate") startDate: String,
        @Path("endDate") endDate: String,
        @Query("from") base: String,
        @Query("to") target: String
    ): HistoryResponse
}