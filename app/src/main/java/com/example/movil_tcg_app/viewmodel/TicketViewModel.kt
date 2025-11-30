package com.example.movil_tcg_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movil_tcg_app.model.TicketApi
import com.example.movil_tcg_app.repository.Repositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TicketUiState(
    val tickets: List<TicketApi> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val mensajeExito: String? = null
)

class TicketViewModel : ViewModel() {

    private val repositorio = Repositorio()

    private val _uiState = MutableStateFlow(TicketUiState())
    val uiState: StateFlow<TicketUiState> = _uiState.asStateFlow()

    init {
        obtenerTickets()
    }

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
                    _uiState.update { it.copy(mensajeExito = "Ticket creado correctamente") }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "No se pudo crear el ticket") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Error al enviar: ${e.message}") }
            }
        }
    }

    fun responderTicket(ticketOriginal: TicketApi, nuevoEstado: String, respuestaAdmin: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val nuevaDescripcion = if (respuestaAdmin.isNotBlank()) {
                "${ticketOriginal.descripcion}\n\n[RESPUESTA ADMIN]: $respuestaAdmin"
            } else {
                ticketOriginal.descripcion
            }

            val ticketActualizado = ticketOriginal.copy(
                estado = nuevoEstado,
                descripcion = nuevaDescripcion
            )

            try {
                val id = ticketOriginal.id ?: return@launch

                val respuesta = repositorio.actualizarTicket(id, ticketActualizado)
                if (respuesta.isSuccessful) {
                    obtenerTickets()
                    _uiState.update { it.copy(mensajeExito = "Ticket actualizado") }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Error al actualizar") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun eliminarTicket(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val respuesta = repositorio.eliminarTicket(id)
                if (respuesta.isSuccessful) {
                    obtenerTickets()
                    _uiState.update { it.copy(mensajeExito = "Ticket eliminado") }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "No se pudo eliminar") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun limpiarMensajes() {
        _uiState.update { it.copy(error = null, mensajeExito = null) }
    }
}