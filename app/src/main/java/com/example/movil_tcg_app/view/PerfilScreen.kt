package com.example.movil_tcg_app.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.EuroSymbol
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.movil_tcg_app.viewmodel.MonedaViewModel
import com.example.movil_tcg_app.viewmodel.UsuarioViewModel

@Composable
fun PerfilScreen(
    viewModel: UsuarioViewModel,
    monedaViewModel: MonedaViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsState()
    val monedaState by monedaViewModel.uiState.collectAsState()
    val usuario = state.loggedInUser

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (usuario != null) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = usuario.usuarioNombre,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = usuario.correo,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Cotización del Día (API Frankfurter)", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Fila Dólar
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AttachMoney, null, tint = Color.Green)
                        Spacer(Modifier.width(8.dp))
                        Text("Dólar: ${monedaState.valorDolarCLP}")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Fila Euro
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.EuroSymbol, null, tint = Color.Blue)
                        Spacer(Modifier.width(8.dp))
                        Text("Euro: ${monedaState.valorEuroCLP}")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("tickets") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.List, contentDescription = null)
                Spacer(modifier = Modifier.size(8.dp))
                Text("Mis Tickets de Soporte")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.logout()
                    navController.navigate(PantallaApp.Inicio.ruta) {
                        popUpTo(0)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null)
                Spacer(modifier = Modifier.size(8.dp))
                Text("Cerrar Sesión")
            }

        } else {
            Text("No hay sesión activa")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate(PantallaApp.Login.ruta) }) {
                Text("Ir a Login")
            }
        }
    }
}