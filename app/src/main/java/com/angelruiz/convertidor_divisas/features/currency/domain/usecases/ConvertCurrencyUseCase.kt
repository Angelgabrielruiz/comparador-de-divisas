package com.angelruiz.convertidor_divisas.features.currency.domain.usecases

import com.angelruiz.convertidor_divisas.features.currency.domain.repositories.CurrencyRepository
import com.angelruiz.convertidor_divisas.features.currency.data.repositories.CurrencyRepositoryImpl // Necesario si no tienes interfaz para historial

class ConvertCurrencyUseCase(private val repository: CurrencyRepository) {

    // Tu función existente de conversión
    suspend operator fun invoke(amount: Double, from: String, to: String): Result<Double> {
        val ratesResult = repository.getExchangeRates(from)
        return ratesResult.mapCatching { rates ->
            val rate = rates.find { it.code == to }?.rate ?: throw Exception("Moneda no encontrada")
            amount * rate
        }
    }

    // Nueva función para el historial (Cast seguro al Impl para acceder al método nuevo)
    suspend fun getHistory(from: String, to: String): List<Double> {
        return if (repository is CurrencyRepositoryImpl) {
            repository.getHistory7Days(from, to)
        } else {
            emptyList()
        }
    }
}