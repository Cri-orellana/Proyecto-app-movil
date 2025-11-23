package com.tcg_project.data.model

data class ProductoApi(
    val productId: Long,
    val franquicia: String,
    val tipo: String,
    val nombreProduto: String,
    val precio: Int,
    val descripcion: String,
    val urlImagen: String
)