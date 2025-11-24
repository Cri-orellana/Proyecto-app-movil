package com.tcg_project.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcg_project.model.TicketApi
import com.tcg_project.repository.Repositorio
import kotlinx.coroutines.launch

class TicketViewModel : ViewModel() {

    private val repositorio = Repositorio()

    private val _listaTickets = MutableLiveData<List<TicketApi>>()
    val listaTickets: LiveData<List<TicketApi>> = _listaTickets

    private val _cargando = MutableLiveData<Boolean>()
    val cargando: LiveData<Boolean> = _cargando

    private val _mensajeError = MutableLiveData<String>()
    val mensajeError: LiveData<String> = _mensajeError

    fun obtenerTickets() {
        viewModelScope.launch {
            _cargando.value = true
            try {
                val respuesta = repositorio.obtenerTickets()
                if (respuesta.isSuccessful) {
                    _listaTickets.value = respuesta.body()
                } else {
                    _mensajeError.value = "Error: ${respuesta.code()}"
                }
            } catch (e: Exception) {
                _mensajeError.value = "Fallo en la conexión: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun crearTicket(ticket: TicketApi) {
        viewModelScope.launch {
            _cargando.value = true
            try {
                val respuesta = repositorio.crearTicket(ticket)
                if (respuesta.isSuccessful) {
                    obtenerTickets()
                } else {
                    _mensajeError.value = "No se pudo crear el ticket"
                }
            } catch (e: Exception) {
                _mensajeError.value = "Error al crear: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun eliminarTicket(id: Int) {
        viewModelScope.launch {
            _cargando.value = true
            try {
                val respuesta =
                    repositorio.eliminarTicket(id) // Necesitas agregar esto en Repositorio también
                if (respuesta.isSuccessful) {
                    // Recargar la lista después de borrar
                    obtenerTickets()
                } else {
                    _mensajeError.value = "Error al eliminar: ${respuesta.code()}"
                }
            } catch (e: Exception) {
                _mensajeError.value = "Error de conexión al eliminar"
            } finally {
                _cargando.value = false
            }
        }
    }
}