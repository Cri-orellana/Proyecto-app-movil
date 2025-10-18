package com.tcg_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.compose.rememberNavController
import com.tcg_project.ui.theme.TcgprojectTheme
import com.tcg_project.view.FormularioScreen
import com.tcg_project.viewmodel.UsuarioViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tcg_project.view.AppScreen
import com.tcg_project.view.CarritoScreen
import com.tcg_project.view.ContactoScreen
import com.tcg_project.view.InicioScreen
import com.tcg_project.view.LoginScreen
import com.tcg_project.view.NosotrosScreen
import com.tcg_project.view.PerfilScreen
import com.tcg_project.view.ProductosScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val usuarioViewModel : UsuarioViewModel = viewModel()
            NavHost(navController = navController, startDestination = AppScreen.Inicio.route) {
                composable(AppScreen.Inicio.route) {
                    InicioScreen(navController)
                }
                composable(AppScreen.Nosotros.route) {
                    NosotrosScreen()
                }
                composable(AppScreen.Contacto.route) {
                    ContactoScreen()
                }
                composable(AppScreen.Login.route) {
                    LoginScreen(navController)
                }
                composable(AppScreen.Registro.route) {
                    FormularioScreen(navController, usuarioViewModel)
                }
                composable(AppScreen.Productos.route) {
                    ProductosScreen()
                }
                composable(AppScreen.Carrito.route) {
                    CarritoScreen()
                }
                composable("Perfil") {
                    PerfilScreen(usuarioViewModel)
                }
            }

        }
    }
}


