package com.tcg_project.data

import com.tcg_project.data.model.ProductoApi
import com.tcg_project.data.model.TicketApi
import com.tcg_project.data.model.UsuarioApi
import com.tcg_project.data.model.UsuarioRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {


    @GET("api/productos")
    suspend fun getAllProductos(): Response<List<ProductoApi>>

    @GET("api/productos/{id}")
    suspend fun getProductoById(@Path("id") id: Long): Response<ProductoApi>

    @POST("api/productos")
    suspend fun createProducto(@Body producto: ProductoApi): Response<ProductoApi>


    @GET("api/tickets")
    suspend fun getAllTickets(): Response<List<TicketApi>>

    @POST("api/tickets")
    suspend fun createTicket(@Body ticket: TicketApi): Response<TicketApi>



    @POST("api/usuarios")
    suspend fun crearUsuario(@Body usuario: UsuarioRequest): Response<UsuarioApi>


}