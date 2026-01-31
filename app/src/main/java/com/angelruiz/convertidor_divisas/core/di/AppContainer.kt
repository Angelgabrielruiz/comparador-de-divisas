package com.angelruiz.convertidor_divisas.core.di

import android.content.Context
import androidx.room.Room
import com.angelruiz.convertidor_divisas.core.network.CurrencyApi
import com.angelruiz.convertidor_divisas.features.currency.data.repositories.CurrencyRepositoryImpl
import com.angelruiz.convertidor_divisas.features.currency.domain.repositories.CurrencyRepository
import com.angelruiz.convertidor_divisas.features.currency.local.CurrencyDatabase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer(private val context: Context) {

    // Frankfurter es pública, no requiere API Key
    private val baseUrl = "https://api.frankfurter.app/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val currencyApi: CurrencyApi by lazy {
        retrofit.create(CurrencyApi::class.java)
    }

    private val database: CurrencyDatabase by lazy {
        Room.databaseBuilder(
            context,
            CurrencyDatabase::class.java,
            "currency_db"
        ).build()
    }

    // Ya no pasamos apiKey al repositorio
    val currencyRepository: CurrencyRepository by lazy {
        CurrencyRepositoryImpl(currencyApi, database.currencyDao())
    }
}