package com.angelruiz.convertidor_divisas.features.currency.data.datasources.remote.model
import com.google.gson.annotations.SerializedName

data class CurrencyResponse(
    @SerializedName("amount") val amount: Double,
    @SerializedName("base") val base: String,
    @SerializedName("date") val date: String,
    @SerializedName("rates") val rates: Map<String, Double>
)