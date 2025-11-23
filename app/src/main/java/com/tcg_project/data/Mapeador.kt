package com.tcg_project.data

import com.tcg_project.data.model.ProductoApi
import com.tcg_project.model.Producto

fun ProductoApi.aProductoUI(): Producto {
    return Producto(
        id = this.productId,
        nombre = this.nombreProduto,
        precio = this.precio.toDouble(),
        franquicia = this.franquicia,
        tipo = this.tipo,
        descripcion = this.descripcion,
        urlImagen = this.urlImagen
    )
}