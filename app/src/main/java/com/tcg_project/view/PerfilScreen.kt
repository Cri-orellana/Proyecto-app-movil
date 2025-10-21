package com.tcg_project.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tcg_project.viewmodel.UsuarioViewModel

@Composable
fun PerfilScreen(viewModel: UsuarioViewModel, navController: NavController) {
    val state by viewModel.state.collectAsState()
    val loggedInUser = state.loggedInUser

    Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
        if (loggedInUser != null) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Perfil de Usuario", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Email: ${loggedInUser.email}")
                Text("Nombre: ${loggedInUser.nombre}")
                Text("Dirección: ${loggedInUser.direccion}")
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {
                        viewModel.logout()
                        // Navegar a una pantalla neutral como Inicio después de cerrar sesión
                        navController.navigate(PantallaApp.Inicio.ruta) {
                            // Limpia toda la pila de navegación para que el usuario no pueda volver atrás
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cerrar Sesión")
                }
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text("No has iniciado sesión", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.navigate(PantallaApp.Login.ruta) }) {
                    Text("Ir a Iniciar Sesión")
                }
            }
        }
    }
}
