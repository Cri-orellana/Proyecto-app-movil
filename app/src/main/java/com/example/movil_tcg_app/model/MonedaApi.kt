package com.example.movil_tcg_app.model

import com.google.gson.annotations.SerializedName

data class MonedaApi(
    val amount: Double,
    val base: String,
    val date: String,

    @SerializedName("rates")
    val tasas: Map<String, Double>
)