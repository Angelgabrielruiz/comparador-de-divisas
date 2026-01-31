package com.angelruiz.convertidor_divisas.features.currency.data.repositories

import com.angelruiz.convertidor_divisas.core.network.CurrencyApi
import com.angelruiz.convertidor_divisas.features.currency.data.datasources.remote.mapper.toDomain
import com.angelruiz.convertidor_divisas.features.currency.local.CurrencyDao
import com.angelruiz.convertidor_divisas.features.currency.local.ExchangeRateEntity
import com.angelruiz.convertidor_divisas.features.currency.domain.entities.CurrencyRate
import com.angelruiz.convertidor_divisas.features.currency.domain.repositories.CurrencyRepository

class CurrencyRepositoryImpl(
    private val api: CurrencyApi,
    private val dao: CurrencyDao, // Recibimos el DAO
    private val apiKey: String
) : CurrencyRepository {

    override suspend fun getExchangeRates(baseCode: String): Result<List<CurrencyRate>> {
        return try {
            // 1. Intentamos obtener datos de la API (Internet)
            val response = api.getLatestRates(apiKey, baseCode)

            if (response.result == "success") {
                val domainList = response.toDomain()

                // 2. Si hay éxito, GUARDAMOS en la Base de Datos (Cache)
                val entities = domainList.map {
                    ExchangeRateEntity(
                        baseCode = baseCode,
                        targetCode = it.code,
                        rate = it.rate
                    )
                }
                dao.insertAll(entities)

                // Devolvemos los datos frescos
                Result.success(domainList)
            } else {
                // Si la API responde error lógico, intentamos local
                getRatesFromLocal(baseCode)
            }

        } catch (e: Exception) {
            // 3. Si falla internet (Exception), buscamos en la Base de Datos
            getRatesFromLocal(baseCode)
        }
    }

    private suspend fun getRatesFromLocal(baseCode: String): Result<List<CurrencyRate>> {
        val localData = dao.getRatesByBase(baseCode)
        return if (localData.isNotEmpty()) {
            // Convertimos de Entidad de Base de Datos -> Dominio
            val domainList = localData.map {
                CurrencyRate(code = it.targetCode, rate = it.rate)
            }
            Result.success(domainList)
        } else {
            Result.failure(Exception("Sin internet y sin datos guardados."))
        }
    }
}