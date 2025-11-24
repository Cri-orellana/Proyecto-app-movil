package com.tcg_project.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.tcg_project.R
import com.tcg_project.viewmodel.CarritoViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun LoadImageCart(url: String?, imageLoader: ImageLoader, modifier: Modifier = Modifier) {
    val painter = rememberAsyncImagePainter(
        model = url ?: "",
        imageLoader = imageLoader,
        error = painterResource(R.drawable.tcg_logo),
        placeholder = painterResource(R.drawable.tcg_logo)
    )
    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
}

@Composable
fun CarritoScreen(viewModel: CarritoViewModel?, imageLoader: ImageLoader) {
    if (viewModel == null) {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
            Text("Inicia sesión para ver tu carrito", style = MaterialTheme.typography.headlineSmall)
        }
        return
    }

    val cartState by viewModel.cartState.collectAsState()

    if (cartState.items.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
            Text("Tu carrito está vacío", style = MaterialTheme.typography.headlineSmall)
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(cartState.items) { cartProduct ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LoadImageCart(
                            url = cartProduct.producto.urlImagen,
                            imageLoader = imageLoader,
                            modifier = Modifier.size(100.dp)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = cartProduct.producto.nombreProduto,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Precio: $${cartProduct.producto.precio}",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = {
                                    viewModel.decrementarCantidad(cartProduct.producto.productId ?: 0L)
                                }) {
                                    Text("-", style = MaterialTheme.typography.headlineSmall)
                                }

                                Text(
                                    text = "${cartProduct.cantidad}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )

                                IconButton(onClick = {
                                    viewModel.incrementarCantidad(cartProduct.producto.productId ?: 0L)
                                }) {
                                    Text("+", style = MaterialTheme.typography.headlineSmall)
                                }
                            }
                        }

                        IconButton(onClick = {
                            viewModel.eliminarDelCarrito(cartProduct.producto.productId ?: 0L)
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar producto")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val formatoMoneda = NumberFormat.getCurrencyInstance(Locale.US) // O Locale.getDefault()
        Text(
            text = "Total: ${formatoMoneda.format(cartState.total)}",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.End)
        )

        Button(
            onClick = { /* TODO: Implementar pago */ },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text("Proceder al Pago")
        }
    }
}