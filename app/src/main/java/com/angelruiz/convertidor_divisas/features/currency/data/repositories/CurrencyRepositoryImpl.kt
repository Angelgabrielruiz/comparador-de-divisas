package com.angelruiz.convertidor_divisas.features.currency.data.repositories

import com.angelruiz.convertidor_divisas.core.network.CurrencyApi
//import com.angelruiz.convertidor_divisas.features.currency.data.local.CurrencyDao
//import com.angelruiz.convertidor_divisas.features.currency.data.local.ExchangeRateEntity
import com.angelruiz.convertidor_divisas.features.currency.domain.entities.CurrencyRate
import com.angelruiz.convertidor_divisas.features.currency.domain.repositories.CurrencyRepository
import com.angelruiz.convertidor_divisas.features.currency.local.CurrencyDao
import com.angelruiz.convertidor_divisas.features.currency.local.ExchangeRateEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CurrencyRepositoryImpl(
    private val api: CurrencyApi,
    private val dao: CurrencyDao
) : CurrencyRepository {

    // 1. Obtener tasas actuales (Lógica Offline incluida)
    override suspend fun getExchangeRates(baseCode: String): Result<List<CurrencyRate>> {
        return try {
            val response = api.getLatestRates(base = baseCode)

            // Mapeo de la respuesta de Frankfurter a nuestro Dominio
            val domainList = response.rates.map { (code, rate) ->
                CurrencyRate(code, rate)
            }

            // Guardar en Cache (Room)
            val entities = domainList.map {
                ExchangeRateEntity(baseCode, it.code, it.rate)
            }
            dao.insertAll(entities)

            Result.success(domainList)

        } catch (e: Exception) {
            // Si falla, intentamos leer de la BD local
            val localData = dao.getRatesByBase(baseCode)
            if (localData.isNotEmpty()) {
                Result.success(localData.map { CurrencyRate(it.targetCode, it.rate) })
            } else {
                Result.failure(e)
            }
        }
    }

    // 2. NUEVA FUNCIÓN: Obtener Historial Real (7 días)
    // No la guardaremos en Room por ahora para no complicar, será "Live"
    suspend fun getHistory7Days(base: String, target: String): List<Double> {
        return try {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

            // Fecha Fin = Hoy
            val endDate = dateFormat.format(calendar.time)

            // Fecha Inicio = Hace 7 días
            calendar.add(Calendar.DAY_OF_YEAR, -7)
            val startDate = dateFormat.format(calendar.time)

            // Llamada a la API
            val response = api.getHistory(startDate, endDate, base, target)

            // La API devuelve un mapa { "2024-01-01": {"MXN": 18.5} }
            // Extraemos solo los valores numéricos ordenados por fecha
            response.rates.toSortedMap().values.mapNotNull { it[target] }

        } catch (e: Exception) {
            emptyList() // Si falla el historial, devolvemos lista vacía (no rompe la app)
        }
    }
}