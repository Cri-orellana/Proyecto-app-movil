package com.tcg_project.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tcg_project.viewmodel.ViewModelLogin // Asegúrate de importar tu ViewModel correcto

@Composable
fun LoginScreen(
    controladorNavegacion: NavController,
    viewModel: ViewModelLogin
) {
    val state by viewModel.estadoUi.collectAsState()

    LaunchedEffect(state.loginExitoso) {
        if (state.loginExitoso) {
            controladorNavegacion.navigate(PantallaApp.Perfil.ruta) {
                popUpTo(PantallaApp.Inicio.ruta)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Iniciar Sesión", style = MaterialTheme.typography.headlineMedium)

        // CAMPO EMAIL
        OutlinedTextField(
            value = state.email,
            onValueChange = { nuevoTexto -> viewModel.cambioEmail(nuevoTexto) },
            label = { Text("Email") },
            isError = state.errores.email != null,
            supportingText = {
                state.errores.email?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.password,
            onValueChange = { nuevoTexto -> viewModel.cambioContrasena(nuevoTexto) },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            isError = state.errores.password != null,
            supportingText = {
                state.errores.password?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            modifier = Modifier.fillMaxWidth()
        )

        state.errorGeneral?.let {
            Spacer(Modifier.height(4.dp))
            Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(horizontal = 8.dp))
        }

        Spacer(Modifier.height(8.dp))

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            Button(
                onClick = { viewModel.login() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar Sesión")
            }

            Button(
                onClick = { controladorNavegacion.navigate(PantallaApp.Registro.ruta) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarse")
            }
        }
    }
}