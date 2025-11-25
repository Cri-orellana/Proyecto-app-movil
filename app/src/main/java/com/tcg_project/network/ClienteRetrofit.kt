package com.tcg_project.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ClienteRetrofit {


    private const val IP_BASE = "10.0.2.2"


    private const val URL_TICKETS = "http://$IP_BASE:8080/"
    private const val URL_PRODUCTOS = "http://$IP_BASE:8081/"
    private const val URL_USUARIOS = "http://$IP_BASE:8082/"

    private fun <T> crearServicio(url: String, serviceClass: Class<T>): T {
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(serviceClass)
    }

    val ticketService: TicketService by lazy {
        crearServicio(URL_TICKETS, TicketService::class.java)
    }

    val productoService: ProductoService by lazy {
        crearServicio(URL_PRODUCTOS, ProductoService::class.java)
    }

    val usuarioService: UsuarioService by lazy {
        crearServicio(URL_USUARIOS, UsuarioService::class.java)
    }
}