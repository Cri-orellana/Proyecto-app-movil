package com.tcg_project.viewmodel

import androidx.lifecycle.ViewModel
import com.tcg_project.model.UsuarioErrores
import com.tcg_project.model.UsuarioUI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class UsuarioViewModel : ViewModel() {
    private val _estado = MutableStateFlow(UsuarioUI())

    val estado: StateFlow<UsuarioUI> = _estado

    fun cambioEmail(valor : String){
        _estado.update { it.copy(email = valor, errores = it.errores.copy(email = null)) }
    }

    fun cambioPassword(valor : String){
        _estado.update { it.copy(password = valor, errores = it.errores.copy(password = null)) }
    }

    fun cambioNombre(valor : String){
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    fun cambioDireccion(valor : String){
        _estado.update { it.copy(direccion = valor, errores = it.errores.copy(direccion = null)) }
    }

    fun cambioAceptarTerminos(valor : Boolean){
        _estado.update { it.copy(aceptaTerminos = valor) }
    }

    fun validacionForm(): Boolean{
        val estadoActual = _estado.value
        val errores = UsuarioErrores(
            email = if(!estadoActual.email.contains("@") && estadoActual.email.length <12) "Correo debe ser ingresado en formato 'ejemplo@ejemplo.com'" else null,
            password = if(estadoActual.password.length < 6) "La contraseña debe tener al menos 6 caracteres" else null,
            nombre = if(estadoActual.nombre.isBlank() || estadoActual.nombre.length < 3) "Debe llenar este campo" else null,
            direccion = if(estadoActual.direccion.length < 6) "Ingrese direccion valida" else null

            )

        val hayErrores = listOfNotNull(
            errores.email,
            errores.password,
            errores.nombre,
            errores.direccion
        ).isNotEmpty()

        _estado.update { it.copy(errores = errores) }

        return !hayErrores
    }
}
