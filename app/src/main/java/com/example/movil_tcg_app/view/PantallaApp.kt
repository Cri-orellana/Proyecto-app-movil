package com.example.movil_tcg_app.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class PantallaApp(
    val ruta: String,
    val titulo: String? = null,
    val icono: ImageVector? = null
) {
    object Inicio : PantallaApp("inicio", "Inicio", Icons.Default.Home)
    object Nosotros : PantallaApp("nosotros", "Nosotros", Icons.Default.Info)
    object Contacto : PantallaApp("contacto", "Contacto", Icons.Default.Email)

    object Login : PantallaApp("login")
    object Registro : PantallaApp("registro")
    object Tickets : PantallaApp("tickets")

    object Carrito : PantallaApp("carrito", "Carrito", Icons.Default.ShoppingCart)
    object Perfil : PantallaApp("perfil", "Perfil", Icons.Default.AccountCircle)

    object AdminPanel : PantallaApp("admin_panel")
    object AgregarProducto : PantallaApp("agregar_producto")

    object GestionarProductos : PantallaApp("gestionar_productos")
    object AdminTickets : PantallaApp("admin_tickets")

    object EditarProducto : PantallaApp("editar_producto/{id}") {
        fun createRoute(id: Long) = "editar_producto/$id"
    }

    object Productos : PantallaApp("productos?franquicia={franquicia}", "Productos", Icons.Default.List) {
        fun conFranquicia(franquicia: String): String {
            return "productos?franquicia=$franquicia"
        }
        const val rutaSinFiltro: String = "productos"
    }
}