package com.tcg_project.model

data class Producto(
    val id: Long,
    val nombre: String,
    val precio: Double,
    val franquicia: String,
    val tipo: String,
    val descripcion: String,
    val urlImagen: String
)