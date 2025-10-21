package com.tcg_project.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tcg_project.Producto
import com.tcg_project.SQLite
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductoViewModel(application: Application) : ViewModel() {

    private val dbHelper = SQLite.getInstance(application)

    private val _allProductos = MutableStateFlow<List<Producto>>(emptyList())
    private val _selectedFranchise = MutableStateFlow<String?>(null)

    private val _productosDestacados = MutableStateFlow<List<Producto>>(emptyList())
    val productosDestacados = _productosDestacados.asStateFlow()

    val selectedFranchise = _selectedFranchise.asStateFlow()

    val franchises: StateFlow<List<String>> = _allProductos.combine(_selectedFranchise) { productos, _ ->
        productos.map { it.franquicia }.distinct()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val productos: StateFlow<List<Producto>> = combine(_allProductos, _selectedFranchise) { productos, selectedFranchise ->
        if (selectedFranchise == null) {
            productos
        } else {
            productos.filter { it.franquicia == selectedFranchise }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            _allProductos.value = dbHelper.getProductos()
            _productosDestacados.value = dbHelper.getArticulosDestacados()
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