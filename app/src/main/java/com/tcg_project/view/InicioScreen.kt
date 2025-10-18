package com.tcg_project.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun InicioScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("TCG-Project LOGO", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { navController.navigate(AppScreen.Nosotros.route) }) {
                Text("Nosotros")
            }
            Button(onClick = { navController.navigate(AppScreen.Contacto.route) }) {
                Text("Contacto")
            }
            Button(onClick = { navController.navigate(AppScreen.Login.route) }) {
                Text("Login")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text("Productos", fontSize = 20.sp)
        Button(onClick = { navController.navigate(AppScreen.Productos.route) }) {
            Text("Ver todos los productos")
        }

    }
}