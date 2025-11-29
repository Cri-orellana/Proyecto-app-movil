package com.example.movil_tcg_app.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ClienteRetrofit {

    private const val URL_TICKETS = "http://98.83.97.82:8080/"

    private const val URL_PRODUCTOS = "http://18.234.130.170:8081/"

    private const val URL_USUARIOS = "http://54.84.50.10:8082/"

    private const val URL_FRANKFURTER = "https://api.frankfurter.app/"


    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private fun <T> crearServicio(url: String, serviceClass: Class<T>): T {
        return Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
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

    val frankfurterService: FrankfurterService by lazy {
        crearServicio(URL_FRANKFURTER, FrankfurterService::class.java)
    }
}