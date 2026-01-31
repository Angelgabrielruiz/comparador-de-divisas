package com.angelruiz.convertidor_divisas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
// Importamos el contenedor de dependencias
import com.angelruiz.convertidor_divisas.core.di.AppContainer
// Importamos el módulo de la feature
import com.angelruiz.convertidor_divisas.features.currency.di.CurrencyModule
// Importamos la PANTALLA (Screen)
import com.angelruiz.convertidor_divisas.features.currency.presentation.screens.ConverterScreen
// Importamos el TEMA (Theme) que acabas de renombrar
import com.angelruiz.convertidor_divisas.ui.theme.AppTheme
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = AppContainer(this)
        val currencyModule = CurrencyModule(appContainer)

        setContent {
            AppTheme {
                ConverterScreen(
                    factory = currencyModule.provideConverterViewModelFactory()
                )
            }
        }
    }
}