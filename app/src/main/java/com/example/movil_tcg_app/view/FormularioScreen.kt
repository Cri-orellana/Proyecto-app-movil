package com.example.movil_tcg_app.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.movil_tcg_app.viewmodel.UsuarioViewModel

@Composable
fun FormularioScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
){
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.loggedInUser) {
        if (state.loggedInUser != null) {
            navController.navigate(PantallaApp.Perfil.ruta) {
                popUpTo(PantallaApp.Inicio.ruta)
            }
        }
    }

    Column(
        Modifier.fillMaxSize().padding(16.dp),
        Arrangement.spacedBy(12.dp)
    ){
        Text("Crear Cuenta", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = state.formEmail,
            onValueChange = viewModel::onEmailChange,
            label = { Text("Email") },
            isError = state.formErrors.email != null,
            supportingText = { state.formErrors.email?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.formPassword,
            onValueChange = viewModel::onPasswordChange,
            label = { Text("Contraseña") },
            isError = state.formErrors.password != null,
            supportingText = { state.formErrors.password?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.formName,
            onValueChange = viewModel::onNameChange,
            label = { Text("Nombre de Usuario") },
            isError = state.formErrors.nombre != null,
            supportingText = { state.formErrors.nombre?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
            modifier = Modifier.fillMaxWidth()
        )


        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = state.formTermsAccepted,
                onCheckedChange = viewModel::onTermsAcceptedChange
            )
            Spacer(Modifier.width(8.dp))
            Text("Acepto los términos y condiciones")
        }

        state.formErrors.errorLogin?.let {
            Text(it, color = Color.Red)
        }

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            Button(
                onClick = { viewModel.register() },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.formTermsAccepted,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    disabledContainerColor = Color.Red.copy(alpha = 0.5f)
                )
            ){
                Text("Registrar")
            }
        }
    }
}