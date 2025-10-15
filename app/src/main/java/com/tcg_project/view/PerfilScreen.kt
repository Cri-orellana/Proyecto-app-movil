package com.tcg_project.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tcg_project.viewmodel.UsuarioViewModel

@Composable
fun PerfilScreen(viewModel: UsuarioViewModel) {
    val estado by viewModel.estado.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Perfil usuario", style = MaterialTheme.typography.headlineMedium)
        Text("Usuario: ${estado.email}")
        Text("Nombre: ${estado.nombre}")
        Text("Dirección: ${estado.direccion}")
        Text("Contraseña: ${"*".repeat(estado.password.length)}")
        Text("¿Términos Aceptados?: ${if (estado.aceptaTerminos) "Aceptados" else "Declinados"}")
    }
}
