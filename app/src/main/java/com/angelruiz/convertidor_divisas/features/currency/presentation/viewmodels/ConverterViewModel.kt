package com.angelruiz.convertidor_divisas.features.currency.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angelruiz.convertidor_divisas.features.currency.domain.usecases.ConvertCurrencyUseCase
import com.angelruiz.convertidor_divisas.features.currency.presentation.screens.ConverterUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConverterViewModel(
    private val convertCurrencyUseCase: ConvertCurrencyUseCase
) : ViewModel() {

    // 1. AÑADIMOS LA LISTA DE MONEDAS AQUÍ
    val supportedCurrencies = listOf("USD", "MXN", "EUR", "GBP", "JPY", "CAD", "AUD", "CNY", "BRL")

    private val _uiState = MutableStateFlow(ConverterUiState())
    val uiState = _uiState.asStateFlow()

    fun convert(amountStr: String, from: String, to: String) {
        val amount = amountStr.toDoubleOrNull()
        if (amount == null) {
            _uiState.update { it.copy(error = "Ingresa un número válido") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = convertCurrencyUseCase(amount, from, to)
            _uiState.update { state ->
                result.fold(
                    onSuccess = { total ->
                        state.copy(isLoading = false, result = total, error = null)
                    },
                    onFailure = { error ->
                        state.copy(isLoading = false, error = error.message)
                    }
                )
            }
        }
    }
}