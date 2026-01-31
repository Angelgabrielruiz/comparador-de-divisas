package com.angelruiz.convertidor_divisas.features.currency.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.angelruiz.convertidor_divisas.features.currency.domain.usecases.ConvertCurrencyUseCase

class ConverterViewModelFactory(
    private val convertCurrencyUseCase: ConvertCurrencyUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConverterViewModel::class.java)) {
            return ConverterViewModel(convertCurrencyUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}