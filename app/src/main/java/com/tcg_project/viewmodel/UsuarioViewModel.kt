package com.tcg_project.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tcg_project.SQLite
import com.tcg_project.Usuario
import com.tcg_project.model.UsuarioErrores
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// 1. Se define el ESTADO ÚNICO Y CENTRALIZADO
data class UserScreenState(
    val loggedInUser: Usuario? = null, // El usuario que ha iniciado sesión
    val formEmail: String = "",
    val formPassword: String = "",
    val formName: String = "",
    val formAddress: String = "",
    val formTermsAccepted: Boolean = false,
    val formErrors: UsuarioErrores = UsuarioErrores()
)

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private val dbHelper = SQLite.getInstance(application)

    // 2. Se crea el StateFlow para el ESTADO ÚNICO
    private val _state = MutableStateFlow(UserScreenState())
    val state: StateFlow<UserScreenState> = _state.asStateFlow()

    init {
        // 3. Se restaura la sesión al iniciar el ViewModel
        viewModelScope.launch {
            val userEmail = prefs.getString("LOGGED_IN_USER_EMAIL", null)
            if (userEmail != null) {
                val user = dbHelper.getUsuarioPorEmail(userEmail)
                _state.update { it.copy(loggedInUser = user) }
            }
        }
    }

    // --- ACCIONES --- (Todas modifican el estado único)

    fun onEmailChange(valor: String) {
        _state.update { it.copy(formEmail = valor, formErrors = it.formErrors.copy(email = null, errorLogin = null)) }
    }

    fun onPasswordChange(valor: String) {
        _state.update { it.copy(formPassword = valor, formErrors = it.formErrors.copy(password = null, errorLogin = null)) }
    }

    fun onNameChange(valor: String) {
        _state.update { it.copy(formName = valor, formErrors = it.formErrors.copy(nombre = null)) }
    }

    fun onAddressChange(valor: String) {
        _state.update { it.copy(formAddress = valor, formErrors = it.formErrors.copy(direccion = null)) }
    }

    fun onTermsAcceptedChange(valor: Boolean) {
        _state.update { it.copy(formTermsAccepted = valor) }
    }

    fun login(): Boolean {
        val currentState = _state.value
        if (currentState.formEmail.isBlank() || currentState.formPassword.isBlank()) {
            _state.update { it.copy(formErrors = it.formErrors.copy(errorLogin = "El email y la contraseña no pueden estar vacíos.")) }
            return false
        }

        val loginSuccessful = dbHelper.loginUsuario(currentState.formEmail, currentState.formPassword)

        return if (loginSuccessful) {
            val user = dbHelper.getUsuarioPorEmail(currentState.formEmail)
            prefs.edit().putString("LOGGED_IN_USER_EMAIL", user?.email).apply()
            _state.update { it.copy(
                loggedInUser = user,
                formEmail = "",
                formPassword = "",
                formErrors = UsuarioErrores()
            )}
            true
        } else {
            _state.update { it.copy(formErrors = it.formErrors.copy(errorLogin = "Usuario o contraseña incorrectos.")) }
            false
        }
    }

    fun logout() {
        prefs.edit().remove("LOGGED_IN_USER_EMAIL").apply()
        _state.update { UserScreenState() } // Resetea todo el estado a su valor inicial
    }

    fun register(): Boolean {
        if (!validateRegistrationForm()) return false
        
        val currentState = _state.value
        dbHelper.registrarUsuario(currentState.formName, currentState.formEmail, currentState.formPassword, currentState.formAddress)

        val user = dbHelper.getUsuarioPorEmail(currentState.formEmail)
        prefs.edit().putString("LOGGED_IN_USER_EMAIL", user?.email).apply()
        
        _state.update { it.copy(
            loggedInUser = user,
            formEmail = "",
            formPassword = "",
            formName = "",
            formAddress = "",
            formTermsAccepted = false,
            formErrors = UsuarioErrores()
        )}
        return true
    }

    private fun validateRegistrationForm(): Boolean {
        val currentState = _state.value
        val errors = UsuarioErrores(
            email = if (!currentState.formEmail.contains("@") || currentState.formEmail.length < 12) "Formato de correo inválido" else null,
            password = if (currentState.formPassword.length < 6) "La contraseña debe tener al menos 6 caracteres" else null,
            nombre = if (currentState.formName.isBlank()) "Debe llenar este campo" else null,
            direccion = if (currentState.formAddress.isBlank()) "Debe llenar este campo" else null
        )
        val hasErrors = listOfNotNull(errors.email, errors.password, errors.nombre, errors.direccion).isNotEmpty()
        _state.update { it.copy(formErrors = errors) }
        return !hasErrors
    }

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UsuarioViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UsuarioViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}