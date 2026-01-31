package com.angelruiz.convertidor_divisas.core.network

import com.angelruiz.convertidor_divisas.features.currency.data.datasources.remote.model.CurrencyResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApi {
    @GET("{apiKey}/latest/{baseCode}")
    suspend fun getLatestRates(
        @Path("apiKey") apiKey: String,
        @Path("baseCode") baseCode: String
    ): CurrencyResponse
}