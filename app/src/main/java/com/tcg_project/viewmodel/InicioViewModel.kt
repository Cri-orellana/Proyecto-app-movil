package com.tcg_project.viewmodel

import androidx.lifecycle.ViewModel
import com.tcg_project.R
import com.tcg_project.model.Franquicia
import com.tcg_project.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class EstadoUiInicio(
    val imagenesCarrusel: List<Int> = emptyList(),
    val franquicias: List<Franquicia> = emptyList(),
    val productosDestacados: List<Producto> = emptyList()
)

class ViewModelInicio : ViewModel() {

    private val _estadoUi = MutableStateFlow(EstadoUiInicio())
    val estadoUi: StateFlow<EstadoUiInicio> = _estadoUi.asStateFlow()

    init {
        cargarDatos()
    }

    private fun cargarDatos() {
        _estadoUi.value = EstadoUiInicio(
            imagenesCarrusel = listOf(
                R.drawable.pokemon_banner,
                R.drawable.yugioh_banner,
                R.drawable.magic_banner,
                R.drawable.mitos_banner
            ),
            franquicias = listOf(
                Franquicia("Pokemon", R.drawable.pokemon_logo),
                Franquicia("Yu-Gi-Oh!", R.drawable.yugioh_logo),
                Franquicia("Magic", R.drawable.magic_logo),
                Franquicia("Mitos", R.drawable.mitos_logo)
            ),
            productosDestacados = listOf(
                Producto("pokemon", 10.990, R.drawable.pokemon_logo),
                Producto("pokemon", 5.990, R.drawable.pokemon_logo),
                Producto("pokemon", 7.990, R.drawable.pokemon_logo)
            )
        )
    }
}
