package com.example.movil_tcg_app.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.movil_tcg_app.R
import com.example.movil_tcg_app.viewmodel.CarritoViewModel
import com.example.movil_tcg_app.viewmodel.ProductoViewModel

@Composable
fun LoadImageProducts(url: String?, imageLoader: ImageLoader, modifier: Modifier = Modifier) {
    AsyncImage(
        model = url ?: "",
        imageLoader = imageLoader,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Fit,
        placeholder = rememberVectorPainter(Icons.Default.Image),
        error = rememberVectorPainter(Icons.Default.BrokenImage)
    )
}

@Composable
fun ProductosScreen(
    productoViewModel: ProductoViewModel,
    carritoViewModel: CarritoViewModel?,
    imageLoader: ImageLoader,
    navController: NavController,
    franquicia: String?
) {
    LaunchedEffect(franquicia) {
        productoViewModel.selectFranchise(franquicia)
    }

    val productos by productoViewModel.productos.collectAsState()
    val franchises by productoViewModel.franchises.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Button(onClick = {
                    navController.navigate(PantallaApp.Productos.rutaSinFiltro) {
                        launchSingleTop = true
                    }
                }) {
                    Text("Todos")
                }
            }

            items(franchises) { franchise ->
                Button(onClick = {
                    navController.navigate(PantallaApp.Productos.conFranquicia(franchise)) {
                        launchSingleTop = true
                    }
                }) {
                    Text(franchise)
                }
            }
        }

        // --- LISTA ---
        if (productos.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay productos disponibles")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(productos) { producto ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            LoadImageProducts(
                                url = producto.urlImagen,
                                imageLoader = imageLoader,
                                modifier = Modifier
                                    .height(200.dp)
                                    .fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = producto.nombreProduto,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "${producto.franquicia} - ${producto.tipo}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = producto.descripcion,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "$ ${producto.precio}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    if (carritoViewModel != null) {
                                        carritoViewModel.agregarAlCarrito(producto.productId ?: 0L)
                                    } else {
                                        navController.navigate(PantallaApp.Login.ruta)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Agregar al Carrito")
                            }
                        }
                    }
                }
            }
        }
    }
}