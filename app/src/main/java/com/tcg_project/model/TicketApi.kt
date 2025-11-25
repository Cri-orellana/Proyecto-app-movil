package com.tcg_project.model

import com.google.gson.annotations.SerializedName

data class TicketApi(
    @SerializedName(value = "id", alternate = ["ticketId"])
    val id: Int? = null,

    val nombre: String = "",
    val email: String = "",
    val asunto: String = "",
    val descripcion: String = "",
    val estado: String = "ABIERTO"
)