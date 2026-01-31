package com.angelruiz.convertidor_divisas.features.currency.di

import com.angelruiz.convertidor_divisas.core.di.AppContainer
import com.angelruiz.convertidor_divisas.features.currency.domain.usecases.ConvertCurrencyUseCase
import com.angelruiz.convertidor_divisas.features.currency.presentation.viewmodels.ConverterViewModelFactory


class CurrencyModule(private val appContainer: AppContainer) {

    fun provideConverterViewModelFactory(): ConverterViewModelFactory {
        return ConverterViewModelFactory(
            convertCurrencyUseCase = ConvertCurrencyUseCase(appContainer.currencyRepository)
        )
    }
}