package com.example.movil_tcg_app.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ContactoScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "¿Tienes algún problema?", fontSize = 20.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Estamos aquí para ayudarte. Si tienes dudas sobre tu pedido o una carta, abre un ticket de soporte.")

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate("tickets") }
        ) {
            Icon(Icons.Default.Email, contentDescription = null)
            Spacer(modifier = Modifier.padding(4.dp))
            Text(text = "Contáctanos / Crear Ticket")
        }
    }
}