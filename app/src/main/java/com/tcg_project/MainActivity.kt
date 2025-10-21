package com.tcg_project

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.ImageLoader
import com.tcg_project.view.BarraNavegacionInferior
import com.tcg_project.view.BarraSuperior
import com.tcg_project.view.CarritoScreen
import com.tcg_project.view.ContactoScreen
import com.tcg_project.view.FormularioScreen
import com.tcg_project.view.LoginScreen
import com.tcg_project.view.NosotrosScreen
import com.tcg_project.view.PantallaApp
import com.tcg_project.view.PantallaInicio
import com.tcg_project.view.PerfilScreen
import com.tcg_project.view.ProductosScreen
import com.tcg_project.viewmodel.CarritoViewModel
import com.tcg_project.viewmodel.ProductoViewModel
import com.tcg_project.viewmodel.UsuarioViewModel

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
                viewModel(key = user.email, factory = CarritoViewModel.Factory(app, user.email))
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
                    composable(PantallaApp.Contacto.ruta) { ContactoScreen() }
                    composable(PantallaApp.Login.ruta) { LoginScreen(controladorNavegacion, usuarioViewModel) }
                    composable(PantallaApp.Registro.ruta) { FormularioScreen(controladorNavegacion, usuarioViewModel) }
                    composable(
                        route = PantallaApp.Productos.ruta,
                        arguments = listOf(navArgument("franquicia") { nullable = true })
                    ) { backStackEntry ->
                        val franquicia = backStackEntry.arguments?.getString("franquicia")
                        ProductosScreen(productoViewModel, carritoViewModel, imageLoader, controladorNavegacion, franquicia)
                    }
                    composable(PantallaApp.Carrito.ruta) {
                        CarritoScreen(carritoViewModel, imageLoader)
                    }
                    composable(PantallaApp.Perfil.ruta) { PerfilScreen(usuarioViewModel, controladorNavegacion) }
                }
            }
        }
    }
}
