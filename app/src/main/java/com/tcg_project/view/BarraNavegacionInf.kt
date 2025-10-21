package com.tcg_project.view

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.tcg_project.viewmodel.ProductoViewModel

@Composable
fun BarraNavegacionInferior(controladorNavegacion: NavController, productoViewModel: ProductoViewModel) {
    val elementos = listOf(
        PantallaApp.Inicio,
        PantallaApp.Productos,
        PantallaApp.Login,
        PantallaApp.Carrito,
        PantallaApp.Nosotros
    )

    NavigationBar(
        containerColor = Color.Red
    ) {
        val pilaNavegacion by controladorNavegacion.currentBackStackEntryAsState()
        val rutaActual = pilaNavegacion?.destination?.route

        elementos.forEach { pantalla ->
            val esRutaProductos = pantalla.ruta.startsWith("pantalla_productos")
            val rutaDestino = if (esRutaProductos) PantallaApp.Productos.sinFiltro() else pantalla.ruta

            NavigationBarItem(
                icon = {
                    pantalla.icono?.let {
                        Icon(it, contentDescription = pantalla.titulo)
                    }
                },
                label = {
                    pantalla.titulo?.let {
                        Text(it)
                    }
                },
                selected = rutaActual == rutaDestino,
                onClick = {
                    // Lógica explícita para limpiar el filtro
                    if (pantalla.ruta == PantallaApp.Inicio.ruta || esRutaProductos) {
                        productoViewModel.selectFranchise(null)
                    }

                    controladorNavegacion.navigate(rutaDestino) {
                        if (pantalla.ruta == PantallaApp.Inicio.ruta) {
                            // Al ir a Inicio, se limpia toda la pila de navegación para un reinicio limpio.
                            popUpTo(controladorNavegacion.graph.findStartDestination().id) {
                                inclusive = true
                            }
                        } else {
                            // Para otras pantallas, se guarda el estado para una navegación más fluida.
                            popUpTo(controladorNavegacion.graph.findStartDestination().id) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.LightGray,
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.LightGray,
                    indicatorColor = Color.Red
                )
            )
        }
    }
}
