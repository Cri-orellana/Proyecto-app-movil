package com.tcg_project.model

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val franquicia: String,
    val imagenUrl: String
)
