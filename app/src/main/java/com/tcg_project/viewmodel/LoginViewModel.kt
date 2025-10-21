package com.tcg_project.viewmodel

import androidx.lifecycle.ViewModel
import com.tcg_project.model.UsuarioErrores
import com.tcg_project.model.UsuarioUI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ViewModelLogin : ViewModel() {

    private val _estadoUi = MutableStateFlow(UsuarioUI())
    val estadoUi = _estadoUi.asStateFlow()

    fun cambioEmail(email: String) {
        _estadoUi.value = _estadoUi.value.copy(email = email)
    }

    fun cambioContrasena(contrasena: String) {
        _estadoUi.value = _estadoUi.value.copy(password = contrasena)
    }

    fun validacionForm(): Boolean {
        val estadoActual = _estadoUi.value
        val erroresNuevos = UsuarioErrores(
            email = if (estadoActual.email.isBlank()) "El email no puede estar vacío" else null,
            password = if (estadoActual.password.isBlank()) "La contraseña no puede estar vacía" else null
        )

        _estadoUi.value = estadoActual.copy(errores = erroresNuevos)

        return erroresNuevos.email == null && erroresNuevos.password == null
    }
}

