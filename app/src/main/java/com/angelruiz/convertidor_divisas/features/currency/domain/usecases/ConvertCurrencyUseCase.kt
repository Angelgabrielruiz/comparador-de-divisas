package com.angelruiz.convertidor_divisas.features.currency.domain.usecases
import com.angelruiz.convertidor_divisas.features.currency.domain.repositories.CurrencyRepository
import kotlin.collections.find

class ConvertCurrencyUseCase(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(amount: Double, from: String, to: String): Result<Double> {
        if (amount <= 0) return Result.failure(Exception("La cantidad debe ser mayor a 0"))

        val result = repository.getExchangeRates(from)

        return result.mapCatching { rates ->
            val targetRate = rates.find { it.code == to }?.rate
                ?: throw Exception("No se encontró la tasa para $to")

            amount * targetRate
        }
    }
}