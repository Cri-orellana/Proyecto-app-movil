package com.tcg_project.model

data class UsuarioUI(
    val email: String = "",
    val password: String = "",
    val errores: UsuarioErrores = UsuarioErrores(),
    val isLoading: Boolean = false,
    val loginExitoso: Boolean = false,
    val errorGeneral: String? = null
)