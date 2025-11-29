package com.example.movil_tcg_app.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movil_tcg_app.viewmodel.MonedaViewModel

@Composable
fun DivisasScreen(
    viewModel: MonedaViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Cotización de Divisas", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(24.dp))

        if (state.isLoading) {
            CircularProgressIndicator()
        } else if (state.error != null) {
            Text("Error: ${state.error}", color = MaterialTheme.colorScheme.error)
            Button(onClick = { viewModel.cargarCotizaciones() }) { Text("Reintentar") }
        } else {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Valores Actualizados (Frankfurter)", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Dólar: ${state.valorDolarCLP}", style = MaterialTheme.typography.headlineMedium)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Euro: ${state.valorEuroCLP}", style = MaterialTheme.typography.headlineMedium)

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "(Precios referenciales en Pesos Chilenos)",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}