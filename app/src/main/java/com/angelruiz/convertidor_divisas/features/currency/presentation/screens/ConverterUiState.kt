package com.angelruiz.convertidor_divisas.features.currency.presentation.screens

data class ConverterUiState(
    val isLoading: Boolean = false,
    val result: Double = 0.0,
    val error: String? = null
)