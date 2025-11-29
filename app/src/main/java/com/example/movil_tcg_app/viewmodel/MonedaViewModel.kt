package com.example.movil_tcg_app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movil_tcg_app.network.ClienteRetrofit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.util.Locale

enum class TipoMoneda {
    CLP, USD, EUR
}

data class MonedaUiState(
    val valorDolarEnClp: Double = 970.0,
    val valorEuroEnUsd: Double = 0.95,
    val monedaSeleccionada: TipoMoneda = TipoMoneda.CLP,
    val isLoading: Boolean = false,
    val error: String? = null
)

class MonedaViewModel : ViewModel() {

    private val servicio = ClienteRetrofit.frankfurterService
    private val _uiState = MutableStateFlow(MonedaUiState())
    val uiState: StateFlow<MonedaUiState> = _uiState.asStateFlow()

    // Formateadores de números
    private val formatoCLP = DecimalFormat("#,###")
    private val formatoExt = DecimalFormat("#,###.00")

    init {
        cargarCotizaciones()
    }

    fun cambiarMoneda(nuevaMoneda: TipoMoneda) {
        _uiState.update { it.copy(monedaSeleccionada = nuevaMoneda) }
    }

    fun convertirPrecio(precioCLP: Int): String {
        val state = _uiState.value

        return when (state.monedaSeleccionada) {
            TipoMoneda.CLP -> {
                "$ ${formatoCLP.format(precioCLP)}"
            }
            TipoMoneda.USD -> {
                val enDolares = if (state.valorDolarEnClp > 0) precioCLP / state.valorDolarEnClp else 0.0
                "US$ ${formatoExt.format(enDolares)}"
            }
            TipoMoneda.EUR -> {
                val enDolares = if (state.valorDolarEnClp > 0) precioCLP / state.valorDolarEnClp else 0.0
                val enEuros = enDolares * state.valorEuroEnUsd
                "€ ${formatoExt.format(enEuros)}"
            }
        }
    }

    fun cargarCotizaciones() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val respuesta = servicio.obtenerCambio("USD", "CLP,EUR")

                if (respuesta.isSuccessful && respuesta.body() != null) {
                    val tasas = respuesta.body()!!.tasas

                    val tasaCLP = tasas["CLP"] ?: 970.0
                    val tasaEUR = tasas["EUR"] ?: 0.95

                    Log.d("MONEDA_API", "CLP: $tasaCLP, EUR: $tasaEUR")

                    _uiState.update { it.copy(
                        isLoading = false,
                        valorDolarEnClp = tasaCLP,
                        valorEuroEnUsd = tasaEUR
                    )}
                } else {
                    Log.e("MONEDA_API", "Error: ${respuesta.code()}")
                    _uiState.update { it.copy(isLoading = false, error = "Error API") }
                }
            } catch (e: Exception) {
                Log.e("MONEDA_API", "Excepción: ${e.message}")
                _uiState.update { it.copy(isLoading = false, error = "Sin conexión") }
            }
        }
    }
}