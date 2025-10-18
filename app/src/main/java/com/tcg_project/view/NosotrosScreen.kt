package com.tcg_project.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NosotrosScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Somos una tienda especializada en Trading Card Games (TCG) fundada por Ignacio Osorio y Cristian Orellana. Nacimos de la pasión compartida por el mundo de las cartas coleccionables y la estrategia que define a este increíble pasatiempo.\n" +
                "            Nuestra misión es ofrecer a la comunidad de jugadores un lugar de confianza donde puedan encontrar los mejores productos, desde los últimos lanzamientos hasta esas cartas específicas que necesitan para completar su colección o su mazo. Nos comprometemos a brindar un servicio de calidad, precios competitivos y una atención cercana y experta para todos nuestros clientes.\n" +
                "            ¡Gracias por ser parte de nuestra comunidad!")
        Text("Dueños: Ignacio Osorio y Cristian Orellana")
    }
}
