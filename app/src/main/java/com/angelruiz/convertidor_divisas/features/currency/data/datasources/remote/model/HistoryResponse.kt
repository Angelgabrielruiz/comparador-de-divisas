package com.angelruiz.convertidor_divisas.features.currency.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

// Ejemplo de respuesta: { "start_date": "...", "rates": { "2024-01-01": {"MXN": 18.5}, ... } }
data class HistoryResponse(
    @SerializedName("amount") val amount: Double,
    @SerializedName("base") val base: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    // El mapa es: Fecha -> (Moneda -> Valor)
    @SerializedName("rates") val rates: Map<String, Map<String, Double>>
)