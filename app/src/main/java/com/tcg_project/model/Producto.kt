package com.tcg_project.model

import androidx.annotation.DrawableRes

data class Producto(
    val nombre: String,
    val precio: Double,
    @DrawableRes val idImagen: Int
)
