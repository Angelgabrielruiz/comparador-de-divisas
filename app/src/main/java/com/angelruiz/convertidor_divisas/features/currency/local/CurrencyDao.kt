package com.angelruiz.convertidor_divisas.features.currency.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrencyDao {
    // Guarda una lista de tasas. Si ya existen, las reemplaza (REPLACE).
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rates: List<ExchangeRateEntity>)

    // Obtiene todas las tasas para una moneda base (ej: "USD")
    @Query("SELECT * FROM exchange_rates WHERE baseCode = :baseCode")
    suspend fun getRatesByBase(baseCode: String): List<ExchangeRateEntity>
}