package com.example.movil_tcg_app

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.ImageLoader
import com.example.movil_tcg_app.view.*
import com.example.movil_tcg_app.viewmodel.*
import com.example.movil_tcg_app.view.TicketScreen

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
            val monedaViewModel: MonedaViewModel = viewModel()

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
                    composable(PantallaApp.Inicio.ruta) {
                        PantallaInicio(
                            navController = controladorNavegacion,
                            productoViewModel = productoViewModel,
                            monedaViewModel = monedaViewModel,
                            imageLoader = imageLoader
                        )
                    }

                    composable(PantallaApp.Nosotros.ruta) { NosotrosScreen() }
                    composable(PantallaApp.Contacto.ruta) { ContactoScreen(controladorNavegacion) }
                    composable(PantallaApp.Login.ruta) { LoginScreen(controladorNavegacion, usuarioViewModel) }
                    composable(PantallaApp.Registro.ruta) { FormularioScreen(controladorNavegacion, usuarioViewModel) }
                    composable(PantallaApp.AdminPanel.ruta) {
                        AdminScreen(controladorNavegacion)
                    }
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

                        ProductosScreen(
                            productoViewModel = productoViewModel,
                            carritoViewModel = carritoViewModel,
                            monedaViewModel = monedaViewModel,
                            imageLoader = imageLoader,
                            navController = controladorNavegacion,
                            franquicia = franquicia
                        )
                    }

                    composable(PantallaApp.Carrito.ruta) {
                        CarritoScreen(carritoViewModel, imageLoader)
                    }

                    composable(PantallaApp.Perfil.ruta) {
                        PerfilScreen(
                            viewModel = usuarioViewModel,
                            monedaViewModel = monedaViewModel,
                            navController = controladorNavegacion
                        )
                    }

                    composable("tickets") {
                        TicketScreen(
                            navController = controladorNavegacion,
                            viewModel = viewModel()
                        )
                    }

                    composable("divisas") {
                        DivisasScreen(viewModel = monedaViewModel)
                    }

                    composable(PantallaApp.AgregarProducto.ruta) {
                        AgregarProductoScreen(controladorNavegacion, productoViewModel)
                    }

                    composable(PantallaApp.BorrarProducto.ruta) {
                        BorrarProductoScreen(
                            navController = controladorNavegacion,
                            viewModel = productoViewModel,
                            imageLoader = imageLoader
                        )
                    }

                    composable(PantallaApp.AdminTickets.ruta) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Pantalla de Gestión de Tickets (En construcción)")
                            Button(onClick = { controladorNavegacion.popBackStack() }, modifier = Modifier.padding(top=32.dp)) {
                                Text("Volver")
                            }
                        }
                    }
                }
            }
        }
    }
}