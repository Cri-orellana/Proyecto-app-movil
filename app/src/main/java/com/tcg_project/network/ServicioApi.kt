package com.tcg_project.network

import com.tcg_project.model.ProductoApi
import com.tcg_project.model.TicketApi
import com.tcg_project.model.UsuarioApi
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ServicioApi {

    // --- TICKETS ---
    @GET("/api/tickets")
    suspend fun obtenerTodosLosTickets(): Response<List<TicketApi>>

    @GET("/api/tickets/{ticket}")
    suspend fun obtenerTicketPorId(@Path("ticket") id: Int): Response<TicketApi>

    @POST("/api/tickets")
    suspend fun crearTicket(@Body ticket: TicketApi): Response<TicketApi>

    @PUT("/api/tickets/{ticket}")
    suspend fun actualizarTicket(@Path("ticket") id: Int, @Body ticket: TicketApi): Response<TicketApi>

    @DELETE("/api/tickets/{ticket}")
    suspend fun eliminarTicket(@Path("ticket") id: Int): Response<Void>

    // --- USUARIOS ---
    @GET("/api/usuarios")
    suspend fun obtenerTodosLosUsuarios(): Response<List<UsuarioApi>>

    @GET("/api/usuarios/{id}")
    suspend fun obtenerUsuarioPorId(@Path("id") id: Int): Response<UsuarioApi>

    @POST("/api/usuarios")
    suspend fun crearUsuario(@Body usuario: UsuarioApi): Response<UsuarioApi>

    @PUT("/api/usuarios/{id}")
    suspend fun actualizarUsuario(@Path("id") id: Int, @Body usuario: UsuarioApi): Response<UsuarioApi>

    @DELETE("/api/usuarios/{id}")
    suspend fun eliminarUsuario(@Path("id") id: Int): Response<Void>

    // --- PRODUCTOS ---
    @GET("/api/productos")
    suspend fun obtenerTodosLosProductos(): Response<List<ProductoApi>>

    @GET("/api/productos/{id}")
    suspend fun obtenerProductoPorId(@Path("id") id: Long): Response<ProductoApi>

    @POST("/api/productos")
    suspend fun crearProducto(@Body producto: ProductoApi): Response<ProductoApi>

    @PUT("/api/productos/{id}")
    suspend fun actualizarProducto(@Path("id") id: Long, @Body producto: ProductoApi): Response<ProductoApi>

    @DELETE("/api/productos/{id}")
    suspend fun eliminarProducto(@Path("id") id: Long): Response<Void>
}