package com.tcg_project.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tcg_project.model.TicketApi
import com.tcg_project.viewmodel.TicketViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketScreen(
    viewModel: TicketViewModel = viewModel()
) {
    val listaTickets by viewModel.listaTickets.observeAsState(emptyList())
    val cargando by viewModel.cargando.observeAsState(false)
    val error by viewModel.mensajeError.observeAsState("")

    var mostrarDialogo by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.obtenerTickets()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Tickets") },
                actions = {
                    IconButton(onClick = { viewModel.obtenerTickets() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Recargar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { mostrarDialogo = true }) {
                Icon(Icons.Default.Add, contentDescription = "Crear Ticket")
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (listaTickets.isEmpty() && !cargando) {
                Text(
                    text = "No hay tickets disponibles",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(listaTickets) { ticket ->
                        TicketItem(
                            ticket = ticket,
                            onDelete = { id -> viewModel.eliminarTicket(id) }
                        )
                    }
                }
            }

            if (cargando) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            if (error.isNotEmpty()) {
                Text(
                    text = error,
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .background(Color.White)
                )
            }
        }
    }

    if (mostrarDialogo) {
        DialogoNuevoTicket(
            onDismiss = { mostrarDialogo = false },
            onConfirm = { nombre, asunto, email, desc ->
                val nuevoTicket = TicketApi(
                    id = null,
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
fun TicketItem(ticket: TicketApi, onDelete: (Int) -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = ticket.asunto, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = "De: ${ticket.nombre} (${ticket.email})", fontSize = 14.sp, color = Color.Gray)
                Text(text = ticket.descripcion, maxLines = 2, fontSize = 14.sp)
                Text(
                    text = "Estado: ${ticket.estado}",
                    color = if (ticket.estado == "ABIERTO") Color.Green else Color.Blue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
            IconButton(onClick = { ticket.id?.let { onDelete(it) } }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
            }
        }
    }
}


@Composable
fun DialogoNuevoTicket(onDismiss: () -> Unit, onConfirm: (String, String, String, String) -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var asunto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Ticket") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
                OutlinedTextField(value = asunto, onValueChange = { asunto = it }, label = { Text("Asunto") })
                OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") })
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(nombre, asunto, email, descripcion) }) {
                Text("Crear")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}