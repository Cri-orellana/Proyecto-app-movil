package com.tcg_project.data.model

data class TicketApi(
    val id: Int,
    val nombre: String,
    val email: String,
    val asunto: String,
    val descripcion: String,
    val estado: String
)