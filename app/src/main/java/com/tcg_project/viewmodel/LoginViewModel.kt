package com.tcg_project.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tcg_project.model.UsuarioErrores
import com.tcg_project.model.UsuarioUI
import com.tcg_project.repository.Repositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelLogin(application: Application) : AndroidViewModel(application) {

    private val repositorio = Repositorio()
    private val prefs = application.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    private val _estadoUi = MutableStateFlow(UsuarioUI())
    val estadoUi = _estadoUi.asStateFlow()

    fun cambioEmail(email: String) {
        _estadoUi.update { it.copy(email = email, errores = it.errores.copy(email = null), errorGeneral = null) }
    }

    fun cambioContrasena(contrasena: String) {
        _estadoUi.update { it.copy(password = contrasena, errores = it.errores.copy(password = null), errorGeneral = null) }
    }

    fun login() {
        if (!validacionForm()) return

        val emailInput = _estadoUi.value.email
        val passwordInput = _estadoUi.value.password

        viewModelScope.launch {
            _estadoUi.update { it.copy(isLoading = true, errorGeneral = null) }

            try {
                val respuesta = repositorio.obtenerUsuarios()

                if (respuesta.isSuccessful) {
                    val listaUsuarios = respuesta.body() ?: emptyList()

                    val usuarioEncontrado = listaUsuarios.find {
                        it.correo == emailInput && it.password == passwordInput
                    }

                    if (usuarioEncontrado != null) {
                        prefs.edit().putString("LOGGED_IN_USER_EMAIL", usuarioEncontrado.correo).apply()

                        // Avisamos a la UI que el login fue correcto
                        _estadoUi.update { it.copy(isLoading = false, loginExitoso = true) }
                    } else {
                        _estadoUi.update { it.copy(isLoading = false, errorGeneral = "Correo o contraseña incorrectos") }
                    }
                } else {
                    _estadoUi.update { it.copy(isLoading = false, errorGeneral = "Error del servidor: ${respuesta.code()}") }
                }
            } catch (e: Exception) {
                _estadoUi.update { it.copy(isLoading = false, errorGeneral = "Sin conexión a internet") }
            }
        }
    }

    private fun validacionForm(): Boolean {
        val estadoActual = _estadoUi.value
        val erroresNuevos = UsuarioErrores(
            email = if (estadoActual.email.isBlank()) "El email no puede estar vacío" else null,
            password = if (estadoActual.password.isBlank()) "La contraseña no puede estar vacía" else null
        )

        _estadoUi.update { it.copy(errores = erroresNuevos) }

        return erroresNuevos.email == null && erroresNuevos.password == null
    }
}
