package com.example.movil_tcg_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movil_tcg_app.network.ClienteRetrofit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.DecimalFormat

data class MonedaUiState(
    val valorDolarCLP: String = "Cargando...",
    val valorEuroCLP: String = "Cargando...",
    val isLoading: Boolean = false,
    val error: String? = null
)

class MonedaViewModel : ViewModel() {

    private val servicio = ClienteRetrofit.frankfurterService

    private val _uiState = MutableStateFlow(MonedaUiState())
    val uiState: StateFlow<MonedaUiState> = _uiState.asStateFlow()
    private val formato = DecimalFormat("#,###")

    init {
        cargarCotizaciones()
    }

    fun cargarCotizaciones() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val respuesta = servicio.obtenerCambio("USD", "CLP,EUR")

                if (respuesta.isSuccessful && respuesta.body() != null) {
                    val tasas = respuesta.body()!!.tasas

                    val dolarEnClp = tasas["CLP"] ?: 0.0
                    val dolarEnEuro = tasas["EUR"] ?: 1.0

                    val euroEnClp = if (dolarEnEuro > 0) dolarEnClp / dolarEnEuro else 0.0

                    _uiState.update { it.copy(
                        isLoading = false,
                        valorDolarCLP = "$ ${formato.format(dolarEnClp)}",
                        valorEuroCLP = "$ ${formato.format(euroEnClp)}"
                    )}
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Error API") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Sin conexión") }
            }
        }
    }
}