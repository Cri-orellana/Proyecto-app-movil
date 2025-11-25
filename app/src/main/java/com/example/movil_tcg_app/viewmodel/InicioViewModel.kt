package com.example.movil_tcg_app.viewmodel

import androidx.lifecycle.ViewModel
import com.example.movil_tcg_app.R
import com.example.movil_tcg_app.model.Franquicia
import com.example.movil_tcg_app.model.ProductoApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class EstadoUiInicio(
    val imagenesCarrusel: List<Int> = emptyList(),
    val franquicias: List<Franquicia> = emptyList(),
    val productosDestacados: List<ProductoApi> = emptyList()
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
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher
            ),
            franquicias = listOf(
                Franquicia("Pokemon", R.mipmap.ic_launcher),
                Franquicia("Yu-Gi-Oh!", R.mipmap.ic_launcher),
                Franquicia("Magic", R.mipmap.ic_launcher),
                Franquicia("Mitos", R.mipmap.ic_launcher)
            ),
            productosDestacados = listOf(
                ProductoApi(
                    productId = 1L,
                    franquicia = "Pokemon",
                    tipo = "Carta",
                    nombreProduto = "Pikachu VMAX",
                    precio = 10990,
                    descripcion = "Carta ultra rara",
                    urlImagen = ""
                ),
                ProductoApi(
                    productId = 2L,
                    franquicia = "Pokemon",
                    tipo = "Booster",
                    nombreProduto = "Sobre Escarlata",
                    precio = 5990,
                    descripcion = "Sobre de expansión",
                    urlImagen = ""
                ),
                ProductoApi(
                    productId = 3L,
                    franquicia = "Pokemon",
                    tipo = "Mazo",
                    nombreProduto = "Mazo Inicio",
                    precio = 7990,
                    descripcion = "Mazo listo para jugar",
                    urlImagen = ""
                )
            )
        )
    }
}