package com.example.movil_tcg_app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.movil_tcg_app.model.ProductoApi
import com.example.movil_tcg_app.repository.Repositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductoViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = Repositorio()

    private val _allProductos = MutableStateFlow<List<ProductoApi>>(emptyList())
    private val _selectedFranchise = MutableStateFlow<String?>(null)

    private val _productosDestacados = MutableStateFlow<List<ProductoApi>>(emptyList())
    val productosDestacados = _productosDestacados.asStateFlow()

    val selectedFranchise = _selectedFranchise.asStateFlow()

    val franchises: StateFlow<List<String>> = _allProductos.combine(_selectedFranchise) { productos, _ ->
        productos.mapNotNull { it.franquicia }.distinct()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val productos: StateFlow<List<ProductoApi>> = combine(_allProductos, _selectedFranchise) { productos, selectedFranchise ->
        if (selectedFranchise == null) {
            productos
        } else {
            productos.filter { it.franquicia?.equals(selectedFranchise, ignoreCase = true) == true }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        cargarProductosDesdeApi()
    }

    private fun cargarProductosDesdeApi() {
        viewModelScope.launch {
            try {
                val respuesta = repositorio.obtenerProductos()

                if (respuesta.isSuccessful) {
                    val listaRemota = respuesta.body() ?: emptyList()
                    _allProductos.value = listaRemota
                    _productosDestacados.value = listaRemota.take(5)
                } else {
                    println("Error al obtener productos: ${respuesta.code()}")
                }
            } catch (e: Exception) {
                println("Error de conexión: ${e.message}")
            }
        }
    }

    fun selectFranchise(franquicia: String?) {
        _selectedFranchise.value = franquicia
    }

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProductoViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProductoViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}