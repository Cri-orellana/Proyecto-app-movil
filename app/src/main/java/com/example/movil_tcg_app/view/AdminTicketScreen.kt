package com.example.movil_tcg_app.view

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.movil_tcg_app.model.TicketApi
import com.example.movil_tcg_app.viewmodel.TicketViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminTicketsScreen(
    navController: NavController,
    viewModel: TicketViewModel
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var ticketSeleccionado by remember { mutableStateOf<TicketApi?>(null) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.obtenerTickets()
    }

    LaunchedEffect(state.mensajeExito) {
        state.mensajeExito?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.limpiarMensajes()
            mostrarDialogo = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Tickets", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Red)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (state.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (state.tickets.isEmpty() && !state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay tickets pendientes.", color = Color.Gray)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(state.tickets) { ticket ->
                        ItemTicketAdmin(
                            ticket = ticket,
                            onEliminar = { ticket.id?.let { viewModel.eliminarTicket(it) } },
                            onEditar = {
                                ticketSeleccionado = ticket
                                mostrarDialogo = true
                            }
                        )
                    }
                }
            }
        }
    }

    if (mostrarDialogo && ticketSeleccionado != null) {
        DialogoGestionarTicket(
            ticket = ticketSeleccionado!!,
            onDismiss = { mostrarDialogo = false },
            onConfirm = { nuevoEstado, respuesta ->
                viewModel.responderTicket(ticketSeleccionado!!, nuevoEstado, respuesta)
            }
        )
    }
}

@Composable
fun ItemTicketAdmin(
    ticket: TicketApi,
    onEliminar: () -> Unit,
    onEditar: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "#${ticket.id}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )

                BadgeEstado(estado = ticket.estado ?: "ABIERTO")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = ticket.asunto,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = ticket.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                Spacer(modifier = Modifier.width(4.dp))
                Text(ticket.nombre, style = MaterialTheme.typography.bodySmall)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Email, null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                Spacer(modifier = Modifier.width(4.dp))
                Text(ticket.email, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onEliminar) {
                    Icon(Icons.Default.DeleteForever, contentDescription = "Eliminar", tint = Color.Red)
                }
                Button(
                    onClick = onEditar,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text("Gestionar", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun BadgeEstado(estado: String) {
    val colorFondo = when (estado) {
        "ABIERTO" -> Color(0xFF4CAF50)
        "EN PROCESO" -> Color(0xFFFF9800)
        "CERRADO" -> Color(0xFFF44336)
        else -> Color.Gray
    }

    Surface(
        color = colorFondo,
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.padding(4.dp)
    ) {
        Text(
            text = estado,
            color = Color.White,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DialogoGestionarTicket(
    ticket: TicketApi,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var estadoSeleccionado by remember { mutableStateOf(ticket.estado ?: "ABIERTO") }
    var respuestaAdmin by remember { mutableStateOf("") }
    val opciones = listOf("ABIERTO", "EN PROCESO", "CERRADO")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Gestionar Ticket #${ticket.id}") },
        text = {
            Column {
                Text("Cambiar Estado:", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                opciones.forEach { opcion ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { estadoSeleccionado = opcion }
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = (opcion == estadoSeleccionado),
                            onClick = { estadoSeleccionado = opcion }
                        )
                        Text(text = opcion, modifier = Modifier.padding(start = 8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = respuestaAdmin,
                    onValueChange = { respuestaAdmin = it },
                    label = { Text("Añadir Respuesta (Opcional)") },
                    placeholder = { Text("Escribe aquí para responder al usuario...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(estadoSeleccionado, respuestaAdmin) }) {
                Text("Guardar Cambios")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}