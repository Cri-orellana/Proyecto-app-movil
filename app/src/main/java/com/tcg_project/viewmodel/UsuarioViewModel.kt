package com.tcg_project.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tcg_project.model.UsuarioApi
import com.tcg_project.model.UsuarioErrores
import com.tcg_project.repository.Repositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UserScreenState(
    val loggedInUser: UsuarioApi? = null,
    val formEmail: String = "",
    val formPassword: String = "",
    val formName: String = "",
    val formTermsAccepted: Boolean = false,
    val formErrors: UsuarioErrores = UsuarioErrores(),
    val isLoading: Boolean = false
)

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private val repositorio = Repositorio()

    private val _state = MutableStateFlow(UserScreenState())
    val state: StateFlow<UserScreenState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val savedEmail = prefs.getString("LOGGED_IN_USER_EMAIL", null)
            if (savedEmail != null) {
                verificarSesionRemota(savedEmail)
            }
        }
    }


    fun onEmailChange(valor: String) {
        _state.update { it.copy(formEmail = valor, formErrors = it.formErrors.copy(email = null, errorLogin = null)) }
    }

    fun onPasswordChange(valor: String) {
        _state.update { it.copy(formPassword = valor, formErrors = it.formErrors.copy(password = null, errorLogin = null)) }
    }

    fun onNameChange(valor: String) {
        _state.update { it.copy(formName = valor, formErrors = it.formErrors.copy(nombre = null)) }
    }

    fun onTermsAcceptedChange(valor: Boolean) {
        _state.update { it.copy(formTermsAccepted = valor) }
    }


    private suspend fun verificarSesionRemota(email: String) {
        try {
            val respuesta = repositorio.obtenerUsuarios()
            if (respuesta.isSuccessful) {
                val usuarioEncontrado = respuesta.body()?.find { it.correo == email }
                if (usuarioEncontrado != null) {
                    _state.update { it.copy(loggedInUser = usuarioEncontrado) }
                }
            }
        } catch (e: Exception) {
        }
    }

    fun login() {
        val currentState = _state.value
        if (currentState.formEmail.isBlank() || currentState.formPassword.isBlank()) {
            _state.update { it.copy(formErrors = it.formErrors.copy(errorLogin = "Campos vacíos")) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val respuesta = repositorio.obtenerUsuarios()

                if (respuesta.isSuccessful) {
                    val listaUsuarios = respuesta.body() ?: emptyList()

                    val usuarioEncontrado = listaUsuarios.find {
                        it.correo == currentState.formEmail && it.password == currentState.formPassword
                    }

                    if (usuarioEncontrado != null) {
                        prefs.edit().putString("LOGGED_IN_USER_EMAIL", usuarioEncontrado.correo).apply()
                        _state.update { it.copy(
                            loggedInUser = usuarioEncontrado,
                            formEmail = "",
                            formPassword = "",
                            formErrors = UsuarioErrores(),
                            isLoading = false
                        )}
                    } else {
                        _state.update { it.copy(
                            isLoading = false,
                            formErrors = it.formErrors.copy(errorLogin = "Credenciales incorrectas")
                        )}
                    }
                } else {
                    _state.update { it.copy(isLoading = false, formErrors = it.formErrors.copy(errorLogin = "Error servidor: ${respuesta.code()}")) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, formErrors = it.formErrors.copy(errorLogin = "Sin conexión")) }
            }
        }
    }

    fun register() {
        if (!validateRegistrationForm()) return

        val currentState = _state.value

        val nuevoUsuario = UsuarioApi(
            id = null,
            usuarioNombre = currentState.formName,
            correo = currentState.formEmail,
            password = currentState.formPassword,
            terminos = currentState.formTermsAccepted
        )

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val respuesta = repositorio.crearUsuario(nuevoUsuario)

                if (respuesta.isSuccessful && respuesta.body() != null) {
                    val usuarioCreado = respuesta.body()!!
                    prefs.edit().putString("LOGGED_IN_USER_EMAIL", usuarioCreado.correo).apply()

                    _state.update { it.copy(
                        loggedInUser = usuarioCreado,
                        formEmail = "",
                        formPassword = "",
                        formName = "",
                        formTermsAccepted = false,
                        formErrors = UsuarioErrores(),
                        isLoading = false
                    )}
                } else {
                    _state.update { it.copy(isLoading = false, formErrors = it.formErrors.copy(errorLogin = "No se pudo registrar")) }
                }
            } catch (e: Exception) {
                Log.e("ERROR_REAL", "--------------------------------------------------")
                Log.e("ERROR_REAL", "CAUSA DEL FALLO: ${e.message}")
                Log.e("ERROR_REAL", "TIPO DE ERROR: ${e.javaClass.simpleName}")
                e.printStackTrace()
                Log.e("ERROR_REAL", "--------------------------------------------------")

                _state.update { it.copy(isLoading = false, formErrors = it.formErrors.copy(errorLogin = "Error: ${e.message}")) }
            }
        }
    }

    fun logout() {
        prefs.edit().remove("LOGGED_IN_USER_EMAIL").apply()
        _state.update { UserScreenState() }
    }

    private fun validateRegistrationForm(): Boolean {
        val currentState = _state.value
        val errors = UsuarioErrores(
            email = if (!currentState.formEmail.contains("@")) "Correo inválido" else null,
            password = if (currentState.formPassword.length < 4) "Mínimo 4 caracteres" else null,
            nombre = if (currentState.formName.isBlank()) "Requerido" else null
        )
        val hasErrors = listOfNotNull(errors.email, errors.password, errors.nombre).isNotEmpty()
        _state.update { it.copy(formErrors = errors) }
        return !hasErrors
    }

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UsuarioViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UsuarioViewModel(app) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}