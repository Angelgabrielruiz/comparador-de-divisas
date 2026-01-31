package com.angelruiz.convertidor_divisas.features.currency.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angelruiz.convertidor_divisas.features.currency.domain.usecases.ConvertCurrencyUseCase
import com.angelruiz.convertidor_divisas.features.currency.presentation.screens.ConverterUiState
import kotlinx.coroutines.async // Para hacer llamadas paralelas
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConverterViewModel(
    private val convertCurrencyUseCase: ConvertCurrencyUseCase
) : ViewModel() {

    // Frankfurter soporta estas y muchas más
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
            // Hacemos las dos cosas al mismo tiempo (Paralelismo) para que sea rápido
            val conversionDeferred = async { convertCurrencyUseCase(amount, from, to) }
            val historyDeferred = async { convertCurrencyUseCase.getHistory(from, to) }

            val conversionResult = conversionDeferred.await()
            val historyResult = historyDeferred.await()

            _uiState.update { state ->
                conversionResult.fold(
                    onSuccess = { total ->
                        state.copy(
                            isLoading = false,
                            result = total,
                            historyData = historyResult, // ¡DATOS REALES!
                            error = null
                        )
                    },
                    onFailure = { error ->
                        state.copy(isLoading = false, error = error.message)
                    }
                )
            }
        }
    }
}