package com.tcg_project.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.ImageLoader
import com.tcg_project.R
import com.tcg_project.viewmodel.ProductoViewModel

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
        Image(painter = painterResource(id = R.drawable.tcg_logo), contentDescription = "Logo", modifier = Modifier.height(100.dp))
        Spacer(modifier = Modifier.height(20.dp))

        Text("Franquicias", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Image(
                painter = painterResource(id = R.drawable.yugioh_logo),
                contentDescription = "Yugioh",
                modifier = Modifier.size(80.dp).clickable { navController.navigate(PantallaApp.Productos.conFranquicia("Yugioh")) },
                contentScale = ContentScale.Fit
            )
            Image(
                painter = painterResource(id = R.drawable.magic_logo),
                contentDescription = "Magic",
                modifier = Modifier.size(80.dp).clickable { navController.navigate(PantallaApp.Productos.conFranquicia("Magic")) },
                contentScale = ContentScale.Fit
            )
            Image(
                painter = painterResource(id = R.drawable.pokemon_logo),
                contentDescription = "Pokemon",
                modifier = Modifier.size(80.dp).clickable { navController.navigate(PantallaApp.Productos.conFranquicia("Pokemon")) },
                contentScale = ContentScale.Fit
            )
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground), // Reemplazar con logo de Mitos
                contentDescription = "Mitos y Leyendas",
                modifier = Modifier.size(80.dp).clickable { navController.navigate(PantallaApp.Productos.conFranquicia("Mitos y Leyendas")) },
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Productos Destacados", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))

        if (productosDestacados.size >= 4) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    productosDestacados.take(2).forEach { producto ->
                        Card(
                            modifier = Modifier.weight(1f).clickable {
                                navController.navigate(PantallaApp.Productos.conFranquicia(producto.franquicia))
                            },
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                LoadImageFromUrl(url = producto.url, imageLoader = imageLoader, modifier = Modifier.height(100.dp))
                                Text(producto.descripcion, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(8.dp))
                            }
                        }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    productosDestacados.drop(2).take(2).forEach { producto ->
                        Card(
                            modifier = Modifier.weight(1f).clickable {
                                navController.navigate(PantallaApp.Productos.conFranquicia(producto.franquicia))
                            },
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                LoadImageFromUrl(url = producto.url, imageLoader = imageLoader, modifier = Modifier.height(100.dp))
                                Text(producto.descripcion, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}