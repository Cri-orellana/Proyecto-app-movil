package com.tcg_project.data.model

data class UsuarioApi(
    val id: Int,
    val usuarioNombre: String,
    val correo: String,
    val password: String,
    val terminos: Boolean
)

data class UsuarioRequest(
    val usuarioNombre: String,
    val correo: String,
    val password: String,
    val terminos: Boolean
)