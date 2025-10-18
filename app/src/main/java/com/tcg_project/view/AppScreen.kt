package com.tcg_project.view

sealed class AppScreen(val route: String) {
    object Inicio : AppScreen("inicio_screen")
    object Nosotros : AppScreen("nosotros_screen")
    object Contacto : AppScreen("contacto_screen")
    object Login : AppScreen("login_screen")
    object Registro : AppScreen("registro_screen")
    object Productos : AppScreen("productos_screen")
    object Carrito : AppScreen("carrito_screen")
}