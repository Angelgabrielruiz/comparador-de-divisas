package com.angelruiz.convertidor_divisas.features.currency.local
import androidx.room.Entity

// La llave primaria compuesta evita duplicados (ej: solo puede haber un registro USD->MXN)
@Entity(tableName = "exchange_rates", primaryKeys = ["baseCode", "targetCode"])
data class ExchangeRateEntity(
    val baseCode: String,
    val targetCode: String,
    val rate: Double,
    val lastUpdated: Long = System.currentTimeMillis()
)