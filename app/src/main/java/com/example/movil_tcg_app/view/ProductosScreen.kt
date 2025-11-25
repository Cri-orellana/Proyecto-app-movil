package com.example.movil_tcg_app.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.movil_tcg_app.viewmodel.CarritoViewModel
import com.example.movil_tcg_app.viewmodel.ProductoViewModel

@Composable
fun LoadImageFromUrl(url: String?, imageLoader: ImageLoader, modifier: Modifier = Modifier) {
    val painter = rememberAsyncImagePainter(
        model = url ?: "",
        imageLoader = imageLoader
    )

    when (painter.state) {
        is AsyncImagePainter.State.Loading -> {
            Icon(
                imageVector = Icons.Default.Downloading,
                contentDescription = null,
                modifier = modifier.padding(20.dp),
                tint = Color.Gray
            )
        }
        is AsyncImagePainter.State.Error -> {
            Icon(
                imageVector = Icons.Default.ImageNotSupported,
                contentDescription = null,
                modifier = modifier.padding(20.dp),
                tint = Color.Red
            )
        }
        else -> {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = modifier,
                contentScale = ContentScale.Fit
            )
        }
    }
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
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Button(onClick = { navController.navigate(PantallaApp.Productos.rutaSinFiltro) }) {
                    Text("Todos")
                }
            }
            items(franchises) { franchise ->
                Button(onClick = {
                    navController.navigate(PantallaApp.Productos.conFranquicia(franchise))
                }) {
                    Text(franchise.uppercase())
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(productos) { producto ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        LoadImageFromUrl(
                            url = producto.urlImagen,
                            imageLoader = imageLoader,
                            modifier = Modifier
                                .size(200.dp)
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

                        Button(onClick = {
                            if (carritoViewModel != null) {
                                carritoViewModel.agregarAlCarrito(producto.productId ?: 0L)
                            } else {
                                navController.navigate(PantallaApp.Login.ruta)
                            }
                        }) {
                            Text("Agregar al Carrito")
                        }
                    }
                }
            }
        }
    }
}