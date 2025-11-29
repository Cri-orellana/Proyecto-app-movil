package com.example.movil_tcg_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movil_tcg_app.repository.Repositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

enum class TipoMoneda {
    CLP, USD, EUR
}

data class MonedaUiState(
    val valorDolarEnClp: Double = 950.0,
    val valorEuroEnUsd: Double = 0.0,

    val monedaSeleccionada: TipoMoneda = TipoMoneda.CLP,

    val isLoading: Boolean = false,
    val error: String? = null
)

class MonedaViewModel : ViewModel() {

    private val repositorio = Repositorio()
    private val _uiState = MutableStateFlow(MonedaUiState())
    val uiState: StateFlow<MonedaUiState> = _uiState.asStateFlow()

    init {
        cargarCotizaciones()
    }

    fun cambiarMoneda(nuevaMoneda: TipoMoneda) {
        _uiState.update { it.copy(monedaSeleccionada = nuevaMoneda) }
    }

    fun formatearPrecio(precioCLP: Int): String {
        val state = _uiState.value

        return when (state.monedaSeleccionada) {
            TipoMoneda.CLP -> {
                "$ $precioCLP"
            }
            TipoMoneda.USD -> {
                // CLP -> USD
                val precioUsd = if (state.valorDolarEnClp > 0) precioCLP / state.valorDolarEnClp else 0.0
                "US$ ${String.format(Locale.US, "%.2f", precioUsd)}"
            }
            TipoMoneda.EUR -> {
                // CLP -> USD -> EUR
                val precioUsd = if (state.valorDolarEnClp > 0) precioCLP / state.valorDolarEnClp else 0.0
                val precioEur = precioUsd * state.valorEuroEnUsd
                "€ ${String.format(Locale.US, "%.2f", precioEur)}"
            }
        }
    }

    fun cargarCotizaciones() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val respuesta = repositorio.obtenerDivisas("USD", "EUR")

                if (respuesta.isSuccessful && respuesta.body() != null) {
                    val tasas = respuesta.body()!!.tasas

                    val tasaEur = tasas["EUR"] ?: 0.0

                    _uiState.update { it.copy(
                        isLoading = false,
                        valorDolarEnClp = 950.0,
                        valorEuroEnUsd = tasaEur
                    )}
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Error API: ${respuesta.code()}") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}