package com.tcg_project.model

data class UsuarioErrores(
    val email: String? = null,
    val password: String? = null,
    val nombre: String? = null,
    val direccion: String? = null,
    val errorLogin: String? = null
)
