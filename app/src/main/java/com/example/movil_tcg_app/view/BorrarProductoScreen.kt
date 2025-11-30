package com.example.movil_tcg_app.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import com.example.movil_tcg_app.viewmodel.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BorrarProductoScreen(
    navController: NavController,
    viewModel: ProductoViewModel,
    imageLoader: ImageLoader
) {
    val productos by viewModel.productos.collectAsState()
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state.mensajeExito) {
        state.mensajeExito?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.limpiarEstados()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Eliminar Productos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (state.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(productos) { producto ->
                    Card(elevation = CardDefaults.cardElevation(4.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LoadImageProducts(url = producto.urlImagen, imageLoader = imageLoader, modifier = Modifier.size(60.dp))

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = producto.nombreProduto, fontWeight = FontWeight.Bold)
                                Text(text = "$ ${producto.precio}", style = MaterialTheme.typography.bodySmall)
                                Text(text = "ID: ${producto.productId}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            }

                            IconButton(
                                onClick = {
                                    producto.productId?.let { id -> viewModel.eliminarProducto(id) }
                                }
                            ) {
                                Icon(Icons.Default.DeleteForever, contentDescription = "Borrar", tint = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}