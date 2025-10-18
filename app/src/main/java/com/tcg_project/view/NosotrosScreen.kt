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
        Text("Objetivo de la página")
        Text("Dueños: Ignacio Osorio y Cristian Orellana")
    }
}
