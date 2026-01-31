package com.angelruiz.convertidor_divisas.features.currency.data.datasources.remote.mapper

import com.angelruiz.convertidor_divisas.features.currency.data.datasources.remote.model.CurrencyResponse // Asegúrate de importar el correcto
import com.angelruiz.convertidor_divisas.features.currency.domain.entities.CurrencyRate

fun CurrencyResponse.toDomain(): List<CurrencyRate> {
    // CORRECCIÓN: Ahora usamos "rates" porque así lo definimos en el nuevo CurrencyResponse
    return this.rates.map { (code, rate) ->
        CurrencyRate(code = code, rate = rate)
    }
}