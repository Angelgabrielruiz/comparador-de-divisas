package com.angelruiz.convertidor_divisas.features.currency.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angelruiz.convertidor_divisas.features.currency.domain.usecases.ConvertCurrencyUseCase
import com.angelruiz.convertidor_divisas.features.currency.presentation.screens.ConverterUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random // <--- IMPORTANTE: Necesario para simular datos

class ConverterViewModel(
    private val convertCurrencyUseCase: ConvertCurrencyUseCase
) : ViewModel() {

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
                        // --- AQUÍ ESTABA EL FALTANTE ---
                        // Generamos los datos simulados para el gráfico
                        val fakeHistory = generateFakeHistory(total)

                        state.copy(
                            isLoading = false,
                            result = total,
                            historyData = fakeHistory, // <--- Guardamos la lista llena
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

    // --- FUNCIÓN NUEVA PARA CREAR DATOS DEL GRÁFICO ---
    private fun generateFakeHistory(currentValue: Double): List<Double> {
        val history = mutableListOf<Double>()
        // Generamos 7 días de datos simulados
        for (i in 0..6) {
            // Variación aleatoria pequeña (entre 98% y 102% del valor real)
            val randomFactor = Random.nextDouble(0.98, 1.02)
            history.add(currentValue * randomFactor)
        }
        return history
    }
}