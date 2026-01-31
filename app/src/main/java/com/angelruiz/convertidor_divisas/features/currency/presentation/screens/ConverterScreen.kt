package com.angelruiz.convertidor_divisas.features.currency.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.angelruiz.convertidor_divisas.features.currency.presentation.components.ResultCard
import com.angelruiz.convertidor_divisas.features.currency.presentation.viewmodels.ConverterViewModel
import com.angelruiz.convertidor_divisas.features.currency.presentation.viewmodels.ConverterViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreen(factory: ConverterViewModelFactory) {

    val viewModel: ConverterViewModel = viewModel(factory = factory)
    val state by viewModel.uiState.collectAsState()

    // 1. OBTENEMOS LA LISTA DESDE EL VIEWMODEL
    val currencies = viewModel.supportedCurrencies

    var amount by remember { mutableStateOf("") }
    // Inicializamos con valores de la lista
    var fromCurrency by remember { mutableStateOf("USD") }
    var toCurrency by remember { mutableStateOf("MXN") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Conversor Clean Arch", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Input Cantidad
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Cantidad") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 2. REEMPLAZAMOS LOS INPUTS MANUALES POR DROPDOWNS
            // Usamos un Row para ponerlos uno al lado del otro, o Column para vertical.
            // Aquí los pongo vertical para que se vean mejor los nombres.

            CurrencyDropdown(
                label = "Desde",
                options = currencies,
                selectedCurrency = fromCurrency,
                onCurrencySelected = { fromCurrency = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            CurrencyDropdown(
                label = "A (Destino)",
                options = currencies,
                selectedCurrency = toCurrency,
                onCurrencySelected = { toCurrency = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón
            Button(
                onClick = { viewModel.convert(amount, fromCurrency, toCurrency) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Convertir", fontSize = 18.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Resultado
            if (state.error != null) {
                Text(
                    text = state.error ?: "Error desconocido",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            } else if (state.result > 0) {
                ResultCard(
                    resultAmount = state.result,
                    currencyCode = toCurrency
                )
            }
        }
    }
}

// 3. COMPONENTE REUTILIZABLE PARA EL DROPDOWN
@Composable
fun CurrencyDropdown(
    label: String,
    options: List<String>,
    selectedCurrency: String,
    onCurrencySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedCurrency,
            onValueChange = {}, // No dejamos escribir, solo seleccionar
            label = { Text(label) },
            readOnly = true, // Teclado oculto
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Seleccionar")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { currency ->
                DropdownMenuItem(
                    text = { Text(text = currency) },
                    onClick = {
                        onCurrencySelected(currency)
                        expanded = false
                    }
                )
            }
        }
    }
}