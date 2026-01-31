package com.angelruiz.convertidor_divisas.core.di

import android.content.Context
import androidx.room.Room
import com.angelruiz.convertidor_divisas.core.network.CurrencyApi
import com.angelruiz.convertidor_divisas.features.currency.local.CurrencyDatabase
import com.angelruiz.convertidor_divisas.features.currency.data.repositories.CurrencyRepositoryImpl
import com.angelruiz.convertidor_divisas.features.currency.domain.repositories.CurrencyRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer(private val context: Context) {

    // Tu API KEY real
    private val apiKey = "e460ae920939cc088043b7a1"
    private val baseUrl = "https://v6.exchangerate-api.com/v6/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val currencyApi: CurrencyApi by lazy {
        retrofit.create(CurrencyApi::class.java)
    }

    // 1. Inicializamos la Base de Datos (Room)
    private val database: CurrencyDatabase by lazy {
        Room.databaseBuilder(
            context,
            CurrencyDatabase::class.java,
            "currency_db"
        ).build()
    }

    // 2. Inyectamos la dependencia (Ahora incluye el DAO de la base de datos)
    val currencyRepository: CurrencyRepository by lazy {
        CurrencyRepositoryImpl(currencyApi, database.currencyDao(), apiKey)
    }
}