package com.example.movil_tcg_app.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.EuroSymbol
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.movil_tcg_app.R
import com.example.movil_tcg_app.viewmodel.MonedaViewModel
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
    monedaViewModel: MonedaViewModel,
    imageLoader: ImageLoader
) {
    val productosDestacados by productoViewModel.productosDestacados.collectAsState()
    val estadoMoneda by monedaViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.tcg_logo),
            contentDescription = "Logo",
            modifier = Modifier.size(100.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.padding(vertical = 16.dp).fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Dólar (CLP)", style = MaterialTheme.typography.labelSmall)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AttachMoney, null, modifier = Modifier.size(16.dp), tint = Color.Green)
                        Text("${estadoMoneda.valorDolarEnClp.toInt()}", fontWeight = FontWeight.Bold)
                    }
                }

                Divider(modifier = Modifier.height(30.dp).width(1.dp))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Euro (vs USD)", style = MaterialTheme.typography.labelSmall)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.EuroSymbol, null, modifier = Modifier.size(16.dp), tint = Color.Blue)
                        Text("${estadoMoneda.valorEuroEnUsd}", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Text("Franquicias", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            BotonFranquicia(navController, "yugioh", "Yu-Gi-Oh!", R.drawable.yugioh_logo)
            BotonFranquicia(navController, "magic", "Magic", R.drawable.magic_logo)
            BotonFranquicia(navController, "pokemon", "Pokemon", R.drawable.pokemon_logo)
            BotonFranquicia(navController, "mitos", "Mitos", R.drawable.mitos_logo)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Destacados", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        if (productosDestacados.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                Text("Cargando...", color = Color.Gray)
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    productosDestacados.take(2).forEach { producto ->
                        CartaProductoInicio(producto, navController, imageLoader)
                    }
                }
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
fun BotonFranquicia(
    navController: NavController,
    idFranquicia: String,
    nombre: String,
    idImagen: Int
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = idImagen),
            contentDescription = nombre,
            modifier = Modifier
                .size(60.dp)
                .clickable {
                    navController.navigate(PantallaApp.Productos.conFranquicia(idFranquicia))
                },
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(nombre, fontSize = 12.sp, fontWeight = FontWeight.Medium)
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
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LoadImageInicio(
                url = producto.urlImagen,
                imageLoader = imageLoader,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .padding(4.dp)
            )
            Text(
                text = producto.nombreProduto,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                maxLines = 1,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "$ ${producto.precio}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}