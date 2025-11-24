package com.tcg_project.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
// IMPORTANTE: Usamos el nuevo modelo ProductoApi
import com.tcg_project.model.ProductoApi
import com.tcg_project.repository.Repositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CarritoProducto(
    val producto: ProductoApi,
    var cantidad: Int
)

data class CartState(
    val items: List<CarritoProducto> = emptyList(),
    val total: Double = 0.0
)

class CarritoViewModel(application: Application, private val usuarioEmail: String) : AndroidViewModel(application) {

    private val repositorio = Repositorio()

    private val _cartState = MutableStateFlow(CartState())
    val cartState = _cartState.asStateFlow()

    private var allProducts: List<ProductoApi> = emptyList()

    private val carritoLocal = mutableListOf<CarritoProducto>()

    init {
        viewModelScope.launch {
            try {
                val respuesta = repositorio.obtenerProductos()
                if (respuesta.isSuccessful) {
                    allProducts = respuesta.body() ?: emptyList()
                }
            } catch (e: Exception) {
            }
        }
    }

    private fun recalcularCarrito() {
        val total = carritoLocal.sumOf { it.producto.precio * it.cantidad.toDouble() }
        _cartState.update {
            it.copy(items = carritoLocal.toList(), total = total)
        }
    }

    fun agregarAlCarrito(productoId: Long) {
        val itemExistente = carritoLocal.find { it.producto.productId == productoId }

        if (itemExistente != null) {
            itemExistente.cantidad++
        } else {
            val productoReal = allProducts.find { it.productId == productoId }

            if (productoReal != null) {
                carritoLocal.add(CarritoProducto(productoReal, 1))
            }
        }
        recalcularCarrito()
    }

    fun eliminarDelCarrito(productoId: Long) {
        val item = carritoLocal.find { it.producto.productId == productoId }
        if (item != null) {
            carritoLocal.remove(item)
            recalcularCarrito()
        }
    }

    fun incrementarCantidad(productoId: Long) {
        agregarAlCarrito(productoId)
    }

    fun decrementarCantidad(productoId: Long) {
        val itemExistente = carritoLocal.find { it.producto.productId == productoId }

        if (itemExistente != null) {
            if (itemExistente.cantidad > 1) {
                itemExistente.cantidad--
            } else {
                carritoLocal.remove(itemExistente)
            }
            recalcularCarrito()
        }
    }

    class Factory(private val app: Application, private val email: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CarritoViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CarritoViewModel(app, email) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }
}