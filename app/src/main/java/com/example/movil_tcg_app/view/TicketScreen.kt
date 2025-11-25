package com.example.movil_tcg_app.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.movil_tcg_app.model.TicketApi
import com.example.movil_tcg_app.viewmodel.TicketViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketScreen(
    navController: NavController,
    viewModel: TicketViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var mostrarDialogo by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.obtenerTickets()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Tickets") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { mostrarDialogo = true }) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Ticket")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            state.error?.let { error ->
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = error)
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = { viewModel.obtenerTickets() }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Reintentar")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (state.tickets.isEmpty() && !state.isLoading) {
                Text("No tienes tickets creados.", color = Color.Gray)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.tickets) { ticket ->
                        Card(elevation = CardDefaults.cardElevation(2.dp)) {
                            Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                                Text(
                                    text = ticket.asunto ?: "Sin Asunto",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Text(
                                    text = ticket.descripcion ?: "",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Estado: ${ticket.estado ?: "ABIERTO"}",
                                    color = if (ticket.estado == "CERRADO") Color.Red else Color.Green,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialogo) {
        DialogoCrearTicket(
            onDismiss = { mostrarDialogo = false },
            onConfirm = { asunto, desc, nombre, email ->
                val nuevoTicket = TicketApi(
                    nombre = nombre,
                    email = email,
                    asunto = asunto,
                    descripcion = desc,
                    estado = "ABIERTO"
                )
                viewModel.crearTicket(nuevoTicket)
                mostrarDialogo = false
            }
        )
    }
}

@Composable
fun DialogoCrearTicket(onDismiss: () -> Unit, onConfirm: (String, String, String, String) -> Unit) {
    var asunto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Ticket") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Tu Nombre") })
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Tu Correo") })
                OutlinedTextField(value = asunto, onValueChange = { asunto = it }, label = { Text("Asunto") })
                OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") })
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(asunto, descripcion, nombre, email) }) {
                Text("Enviar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}