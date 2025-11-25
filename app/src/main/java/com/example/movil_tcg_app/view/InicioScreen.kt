package com.example.movil_tcg_app.view

import android.R
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
        Text("TCG-Project", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { navController.navigate(PantallaApp.Nosotros.ruta) }) {
                Text("Nosotros")
            }
            Button(onClick = { navController.navigate(PantallaApp.Contacto.ruta) }) {
                Text("Contacto")
            }
            Button(onClick = { navController.navigate(PantallaApp.Login.ruta) }) {
                Text("Login")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text("Productos", fontSize = 20.sp)
        Button(onClick = { navController.navigate(PantallaApp.Productos.ruta) }) {
            Text("Ver todos los productos")
        }

    }
}