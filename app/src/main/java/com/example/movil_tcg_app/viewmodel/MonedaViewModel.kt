package com.example.movil_tcg_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movil_tcg_app.repository.Repositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MonedaUiState(
    val valorDolar: Double = 0.0,
    val valorEuro: Double = 0.0,
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

    fun cargarCotizaciones() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val respuesta = repositorio.obtenerDivisas("USD", "CLP,EUR")

                if (respuesta.isSuccessful && respuesta.body() != null) {
                    val tasas = respuesta.body()!!.tasas
                    _uiState.update { it.copy(
                        isLoading = false,
                        valorDolar = tasas["CLP"] ?: 0.0,
                        valorEuro = tasas["EUR"] ?: 0.0
                    )}
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Error API") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}