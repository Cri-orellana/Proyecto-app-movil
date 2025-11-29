package com.example.movil_tcg_app.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.movil_tcg_app.viewmodel.ProductoViewModel

@Composable
fun LoadImageInicio(url: String?, imageLoader: ImageLoader, modifier: Modifier = Modifier) {
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
fun PantallaInicio(
    navController: NavController,
    productoViewModel: ProductoViewModel,
    imageLoader: ImageLoader
) {
    val productosDestacados by productoViewModel.productosDestacados.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Logo",
            modifier = Modifier.size(100.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text("Franquicias", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            BotonFranquicia(navController, "yugioh", "Yu-Gi-Oh!")
            BotonFranquicia(navController, "magic", "Magic")
            BotonFranquicia(navController, "pokemon", "Pokemon")
            BotonFranquicia(navController, "mitos", "Mitos")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Productos Destacados", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))

        if (productosDestacados.isEmpty()) {
            Text("Cargando productos...", color = Color.Gray)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Fila 1
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    productosDestacados.take(2).forEach { producto ->
                        CartaProductoInicio(producto, navController, imageLoader)
                    }
                }
                // Fila 2
                if (productosDestacados.size > 2) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        productosDestacados.drop(2).take(2).forEach { producto ->
                            CartaProductoInicio(producto, navController, imageLoader)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BotonFranquicia(navController: NavController, idFranquicia: String, nombre: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = nombre,
            modifier = Modifier
                .size(60.dp)
                .clickable {
                    navController.navigate(PantallaApp.Productos.conFranquicia(idFranquicia))
                },
            tint = MaterialTheme.colorScheme.secondary
        )
        Text(nombre, fontSize = 12.sp)
    }
}

@Composable
fun RowScope.CartaProductoInicio(
    producto: com.example.movil_tcg_app.model.ProductoApi,
    navController: NavController,
    imageLoader: ImageLoader
) {
    Card(
        modifier = Modifier
            .weight(1f)
            .clickable {
                val franquicia = producto.franquicia ?: "pokemon"
                navController.navigate(PantallaApp.Productos.conFranquicia(franquicia))
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LoadImageInicio(
                url = producto.urlImagen,
                imageLoader = imageLoader,
                modifier = Modifier.height(100.dp).fillMaxWidth()
            )
            Text(
                text = producto.nombreProduto,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(8.dp),
                maxLines = 1
            )
            Text(
                text = "$ ${producto.precio}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}

