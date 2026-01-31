package com.angelruiz.convertidor_divisas.features.currency.data.datasources.remote.mapper


import com.angelruiz.convertidor_divisas.features.currency.data.datasources.remote.model.CurrencyResponse
import com.angelruiz.convertidor_divisas.features.currency.domain.entities.CurrencyRate

fun CurrencyResponse.toDomain(): List<CurrencyRate> {
    return this.conversionRates.map { (code, rate) ->
        CurrencyRate(code = code, rate = rate)
    }
}
