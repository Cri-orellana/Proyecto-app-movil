package com.example.movil_tcg_app.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.movil_tcg_app.viewmodel.TipoMoneda
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

    val listaAdmins = listOf("cris@tienda.cl", "nacho@tienda.cl")

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
            Text(text = usuario.correo, color = Color.Gray)

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Preferencias de Moneda", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        BotonMoneda("CLP", TipoMoneda.CLP, monedaState.monedaSeleccionada, monedaViewModel)
                        BotonMoneda("USD", TipoMoneda.USD, monedaState.monedaSeleccionada, monedaViewModel)
                        BotonMoneda("EUR", TipoMoneda.EUR, monedaState.monedaSeleccionada, monedaViewModel)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "1 USD = $ ${monedaState.valorDolarEnClp.toInt()} CLP",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
            }

            if (listaAdmins.contains(usuario.correo)) {
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate(PantallaApp.AdminPanel.ruta) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("Panel de Administrador", color = Color.White)
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

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.logout()
                    navController.navigate(PantallaApp.Inicio.ruta) { popUpTo(0) }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar Sesión")
            }

        } else {
            Text("No hay sesión activa")
            Button(onClick = { navController.navigate(PantallaApp.Login.ruta) }) {
                Text("Ir a Login")
            }
        }
    }
}

@Composable
fun BotonMoneda(
    texto: String,
    tipo: TipoMoneda,
    seleccionada: TipoMoneda,
    viewModel: MonedaViewModel
) {
    val esSeleccionada = tipo == seleccionada
    Button(
        onClick = { viewModel.cambiarMoneda(tipo) },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (esSeleccionada) MaterialTheme.colorScheme.primary else Color.LightGray,
            contentColor = if (esSeleccionada) Color.White else Color.Black
        )
    ) {
        Text(texto)
    }
}