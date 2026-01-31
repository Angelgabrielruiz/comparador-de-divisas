package com.angelruiz.convertidor_divisas.features.currency.domain.repositories
import com.angelruiz.convertidor_divisas.features.currency.domain.entities.CurrencyRate

interface CurrencyRepository {
    suspend fun getExchangeRates(baseCode: String): Result<List<CurrencyRate>>
}