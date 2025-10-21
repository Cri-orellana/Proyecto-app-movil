package com.tcg_project.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tcg_project.CarritoItem
import com.tcg_project.Producto
import com.tcg_project.SQLite
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CarritoProducto(val producto: Producto, val cantidad: Int)
data class CartState(val items: List<CarritoProducto> = emptyList(), val total: Double = 0.0)

class CarritoViewModel(application: Application, private val usuarioEmail: String) : ViewModel() {

    private val dbHelper = SQLite.getInstance(application)

    private val _cartState = MutableStateFlow(CartState())
    val cartState = _cartState.asStateFlow()

    private var allProducts: List<Producto> = emptyList()

    init {
        viewModelScope.launch {
            allProducts = dbHelper.getProductos()
            refreshCart()
        }
    }

    private fun refreshCart() {
        viewModelScope.launch {
            val cartItems = dbHelper.obtenerItemsDelCarrito(usuarioEmail)
            val enrichedItems = cartItems.mapNotNull { cartItem ->
                allProducts.find { it.id == cartItem.productoId }?.let {
                    CarritoProducto(it, cartItem.cantidad)
                }
            }
            val total = enrichedItems.sumOf { it.producto.precio * it.cantidad.toDouble() }
            _cartState.value = CartState(items = enrichedItems, total = total)
        }
    }

    fun agregarAlCarrito(productoId: String) {
        viewModelScope.launch {
            val existingItem = dbHelper.obtenerItemsDelCarrito(usuarioEmail).find { it.productoId == productoId }
            if (existingItem != null) {
                existingItem.cantidad++
                dbHelper.agregarAlCarrito(existingItem)
            } else {
                val newItem = CarritoItem(productoId, usuarioEmail, 1)
                dbHelper.agregarAlCarrito(newItem)
            }
            refreshCart()
        }
    }

    fun eliminarDelCarrito(productoId: String) {
        viewModelScope.launch {
            dbHelper.eliminarItemDelCarrito(productoId, usuarioEmail)
            refreshCart()
        }
    }

    fun incrementarCantidad(productoId: String) {
        agregarAlCarrito(productoId) // La lógica es la misma
    }

    fun decrementarCantidad(productoId: String) {
        viewModelScope.launch {
            val existingItem = dbHelper.obtenerItemsDelCarrito(usuarioEmail).find { it.productoId == productoId }
            if (existingItem != null) {
                if (existingItem.cantidad > 1) {
                    existingItem.cantidad--
                    dbHelper.agregarAlCarrito(existingItem)
                } else {
                    dbHelper.eliminarItemDelCarrito(productoId, usuarioEmail)
                }
            }
            refreshCart()
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