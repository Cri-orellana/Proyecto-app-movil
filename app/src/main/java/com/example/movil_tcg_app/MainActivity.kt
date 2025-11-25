package com.example.movil_tcg_app

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.ImageLoader
import com.example.movil_tcg_app.view.BarraNavegacionInferior
import com.example.movil_tcg_app.view.BarraSuperior
import com.example.movil_tcg_app.view.CarritoScreen
import com.example.movil_tcg_app.view.ContactoScreen
import com.example.movil_tcg_app.view.FormularioScreen
import com.example.movil_tcg_app.view.LoginScreen
import com.example.movil_tcg_app.view.NosotrosScreen
import com.example.movil_tcg_app.view.PantallaApp
import com.example.movil_tcg_app.view.PantallaInicio
import com.example.movil_tcg_app.view.PerfilScreen
import com.example.movil_tcg_app.view.ProductosScreen
import com.example.movil_tcg_app.view.DivisasScreen
import com.example.movil_tcg_app.view.TicketScreen
import com.example.movil_tcg_app.viewmodel.CarritoViewModel
import com.example.movil_tcg_app.viewmodel.ProductoViewModel
import com.example.movil_tcg_app.viewmodel.UsuarioViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val controladorNavegacion = rememberNavController()
            val context = LocalContext.current
            val app = context.applicationContext as Application

            val usuarioViewModel: UsuarioViewModel = viewModel(factory = UsuarioViewModel.Factory(app))
            val productoViewModel: ProductoViewModel = viewModel(factory = ProductoViewModel.Factory(app))

            val imageLoader = ImageLoader.Builder(context).build()

            val userState by usuarioViewModel.state.collectAsState()
            val carritoViewModel: CarritoViewModel? = userState.loggedInUser?.let { user ->
                viewModel(key = user.correo, factory = CarritoViewModel.Factory(app, user.correo))
            }

            Scaffold(
                topBar = { BarraSuperior() } ,
                bottomBar = { BarraNavegacionInferior(controladorNavegacion, productoViewModel) }
            ) { paddingValues ->
                NavHost(
                    navController = controladorNavegacion,
                    startDestination = PantallaApp.Inicio.ruta,
                    modifier = Modifier.padding(paddingValues)
                ) {
                    composable(PantallaApp.Inicio.ruta) { PantallaInicio(controladorNavegacion, productoViewModel, imageLoader) }
                    composable(PantallaApp.Nosotros.ruta) { NosotrosScreen() }
                    composable(PantallaApp.Contacto.ruta) { ContactoScreen(controladorNavegacion) }
                    composable(PantallaApp.Login.ruta) { LoginScreen(controladorNavegacion, usuarioViewModel) }
                    composable(PantallaApp.Registro.ruta) { FormularioScreen(controladorNavegacion, usuarioViewModel) }

                    composable(
                        route = "productos?franquicia={franquicia}",
                        arguments = listOf(
                            navArgument("franquicia") {
                                nullable = true
                                defaultValue = null
                                type = NavType.StringType
                            }
                        )
                    ) { backStackEntry ->
                        val franquicia = backStackEntry.arguments?.getString("franquicia")
                        ProductosScreen(productoViewModel, carritoViewModel, imageLoader, controladorNavegacion, franquicia)
                    }

                    composable(PantallaApp.Carrito.ruta) {
                        CarritoScreen(carritoViewModel, imageLoader)
                    }

                    composable(PantallaApp.Perfil.ruta) { PerfilScreen(usuarioViewModel, controladorNavegacion) }

                    composable("tickets") {
                        TicketScreen(navController = controladorNavegacion)
                    }

                    composable("divisas") { DivisasScreen() }
                }
            }
        }
    }
}