package com.app_tcg.ui.theme.Views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.w3c.dom.Text

//Usar el * para importar todas las funciones disponibles de un import

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginView(navController: NavController){

    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Scaffold(
        //topBar = { TopAppBar(title = { Text("Login") }) }
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Login")
                }
            ) }

    )
    { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it},
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            if (error.isNotEmpty()){
                Text( error, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
            }
            Button(
                onClick = {
                    if (usuario.isBlank() || password.isBlank()){
                        error = "Debe ingresar usuario y contraseña"
                    }else if (usuario.length<2){
                        error = "Usuario debe tener por lo menos 2 caracter"
                    }else if (password.length<4){
                        error = "La contraseña debe tener por lo menos 4 caracteres"
                    }else{
                        error = ""
                        //Navego hacia la view NotasScreen
                        navController.navigate("notas")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Inicio de sesión")
            }
            TextButton(onClick = { navController.navigate("registro")}) {
                Text("¿No tienes cuenta? Registrate")
            }
        }
    }

}