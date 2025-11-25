package com.tcg_project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcg_project.model.TicketApi
import com.tcg_project.repository.Repositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TicketUiState(
    val tickets: List<TicketApi> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class TicketViewModel : ViewModel() {

    private val repositorio = Repositorio()

    private val _uiState = MutableStateFlow(TicketUiState())
    val uiState: StateFlow<TicketUiState> = _uiState.asStateFlow()

    fun obtenerTickets() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val respuesta = repositorio.obtenerTickets()
                if (respuesta.isSuccessful) {
                    _uiState.update { it.copy(
                        tickets = respuesta.body() ?: emptyList(),
                        isLoading = false
                    )}
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Error servidor: ${respuesta.code()}") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Error conexión: ${e.message}") }
            }
        }
    }

    fun crearTicket(nuevoTicket: TicketApi) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val respuesta = repositorio.crearTicket(nuevoTicket)
                if (respuesta.isSuccessful) {
                    obtenerTickets()
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "No se pudo crear el ticket") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Error al enviar: ${e.message}") }
            }
        }
    }

    fun limpiarError() {
        _uiState.update { it.copy(error = null) }
    }
}