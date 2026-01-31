package com.angelruiz.convertidor_divisas.features.currency.data.datasources.remote.model
import com.google.gson.annotations.SerializedName

data class CurrencyResponse(
    @SerializedName("result") val result: String,
    @SerializedName("base_code") val baseCode: String,
    @SerializedName("conversion_rates") val conversionRates: Map<String, Double>
)