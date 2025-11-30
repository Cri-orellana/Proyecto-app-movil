package com.example.movil_tcg_app.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.movil_tcg_app.model.ProductoApi
import com.example.movil_tcg_app.viewmodel.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarProductoScreen(
    navController: NavController,
    viewModel: ProductoViewModel
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val listaFranquicias = listOf("pokemon", "yugioh", "magic", "mitos")
    val listaTipos = listOf("cartas", "decks", "boxes", "accesorios")

    var nombre by remember { mutableStateOf("") }

    var franquicia by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }

    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var urlImagen by remember { mutableStateOf("") }

    LaunchedEffect(state.mensajeExito) {
        state.mensajeExito?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.limpiarEstados()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Producto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del Producto") },
                modifier = Modifier.fillMaxWidth()
            )

            DropdownInput(
                label = "Selecciona Franquicia",
                options = listaFranquicias,
                selectedOption = franquicia,
                onOptionSelected = { franquicia = it }
            )

            DropdownInput(
                label = "Selecciona Tipo de Producto",
                options = listaTipos,
                selectedOption = tipo,
                onOptionSelected = { tipo = it }
            )

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio (CLP)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            OutlinedTextField(
                value = urlImagen,
                onValueChange = { urlImagen = it },
                label = { Text("URL de Imagen") },
                modifier = Modifier.fillMaxWidth()
            )

            if (state.error != null) {
                Text(text = state.error ?: "", color = Color.Red)
            }

            Button(
                onClick = {
                    if (nombre.isNotBlank() && precio.isNotBlank() && franquicia.isNotBlank() && tipo.isNotBlank()) {
                        val nuevo = ProductoApi(
                            productId = null,
                            nombreProduto = nombre,
                            franquicia = franquicia,
                            tipo = tipo,
                            precio = precio.toIntOrNull() ?: 0,
                            descripcion = descripcion,
                            urlImagen = urlImagen
                        )
                        viewModel.crearProducto(nuevo)
                    } else {
                        Toast.makeText(context, "Complete todos los campos requeridos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Guardar Producto")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownInput(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}