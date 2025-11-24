package com.tcg_project.model

data class UsuarioApi(
    val id: Int?,
    val usuarioNombre: String,
    val correo: String,
    val password: String,
    val terminos: Boolean
)