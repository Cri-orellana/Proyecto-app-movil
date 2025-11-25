package com.example.movil_tcg_app.view

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
import com.example.movil_tcg_app.viewmodel.ProductoViewModel

@Composable
fun BarraNavegacionInferior(
    navController: NavController,
    productoViewModel: ProductoViewModel
) {
    val pantallas = listOf(
        PantallaApp.Inicio,
        PantallaApp.Productos,
        PantallaApp.Carrito,
        PantallaApp.Perfil,
        PantallaApp.Contacto
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val mostrarBarra = pantallas.any { pantalla ->
        if (pantalla == PantallaApp.Productos) {
            currentRoute?.startsWith("productos") == true
        } else {
            currentRoute == pantalla.ruta
        }
    }

    if (mostrarBarra) {
        NavigationBar(
            containerColor = Color.Red,
            contentColor = Color.White
        ) {
            pantallas.forEach { pantalla ->

                val selected = if (pantalla == PantallaApp.Productos) {
                    currentRoute?.startsWith("productos") == true
                } else {
                    currentRoute == pantalla.ruta
                }

                NavigationBarItem(
                    icon = {
                        if (pantalla.icono != null) {
                            Icon(imageVector = pantalla.icono, contentDescription = pantalla.titulo)
                        }
                    },
                    label = {
                        if (pantalla.titulo != null) {
                            Text(pantalla.titulo)
                        }
                    },
                    selected = selected,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Red,
                        selectedTextColor = Color.White,
                        indicatorColor = Color.White,
                        unselectedIconColor = Color.White.copy(alpha = 0.7f),
                        unselectedTextColor = Color.White.copy(alpha = 0.7f)
                    ),
                    onClick = {
                        val rutaDestino = if (pantalla == PantallaApp.Productos) {
                            PantallaApp.Productos.rutaSinFiltro
                        } else {
                            pantalla.ruta
                        }

                        navController.navigate(rutaDestino) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                if (pantalla != PantallaApp.Inicio) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            if (pantalla != PantallaApp.Inicio) {
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }
}