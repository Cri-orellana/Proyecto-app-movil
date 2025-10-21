package com.tcg_project.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.ui.graphics.vector.ImageVector

sealed class PantallaApp(val ruta: String, val titulo: String? = null, val icono: ImageVector? = null) {
    object Inicio : PantallaApp("pantalla_inicio", "Inicio", Icons.Default.Home)
    object Productos : PantallaApp("pantalla_productos?franquicia={franquicia}", "Productos", Icons.Default.Store) {
        fun conFranquicia(franquicia: String) = "pantalla_productos?franquicia=$franquicia"
        fun sinFiltro() = "pantalla_productos"
    }
    object Login : PantallaApp("pantalla_login", "Cuenta", Icons.Default.Person)
    object Registro : PantallaApp("pantalla_registro")
    object Carrito : PantallaApp("pantalla_carrito", "Carrito", Icons.Default.ShoppingCart)
    object Nosotros : PantallaApp("pantalla_nosotros", "Nosotros", Icons.Default.Info)
    object Contacto : PantallaApp("pantalla_contacto")
    object Perfil : PantallaApp("pantalla_perfil")
}
