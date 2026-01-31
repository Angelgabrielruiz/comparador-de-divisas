package com.angelruiz.convertidor_divisas.features.currency.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.angelruiz.convertidor_divisas.features.currency.local.ExchangeRateEntity

@Database(entities = [ExchangeRateEntity::class], version = 1)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}