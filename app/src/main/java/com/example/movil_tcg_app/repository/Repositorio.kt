package com.example.movil_tcg_app.repository

import com.example.movil_tcg_app.model.MonedaApi
import com.example.movil_tcg_app.model.ProductoApi
import com.example.movil_tcg_app.model.TicketApi
import com.example.movil_tcg_app.model.UsuarioApi
import com.example.movil_tcg_app.network.ClienteRetrofit
import retrofit2.Response

class Repositorio {

    private val apiTickets = ClienteRetrofit.ticketService
    private val apiProductos = ClienteRetrofit.productoService
    private val apiUsuarios = ClienteRetrofit.usuarioService
    private val apiMonedas = ClienteRetrofit.frankfurterService

    suspend fun obtenerTickets(): Response<List<TicketApi>> {
        return apiTickets.obtenerTodosLosTickets()
    }

    suspend fun crearTicket(ticket: TicketApi): Response<TicketApi> {
        return apiTickets.crearTicket(ticket)
    }

    suspend fun actualizarTicket(id: Int, ticket: TicketApi): Response<TicketApi> {
        return apiTickets.actualizarTicket(id, ticket)
    }

    suspend fun eliminarTicket(id: Int): Response<Void> {
        return apiTickets.eliminarTicket(id)
    }

    suspend fun obtenerUsuarios(): Response<List<UsuarioApi>> {
        return apiUsuarios.obtenerTodosLosUsuarios()
    }

    suspend fun crearUsuario(usuario: UsuarioApi): Response<UsuarioApi> {
        return apiUsuarios.crearUsuario(usuario)
    }

    suspend fun obtenerProductos(): Response<List<ProductoApi>> {
        return apiProductos.obtenerTodosLosProductos()
    }

    suspend fun crearProducto(producto: ProductoApi): Response<ProductoApi> {
        return apiProductos.crearProducto(producto)
    }

    suspend fun actualizarProducto(id: Long, producto: ProductoApi): Response<ProductoApi> {
        return apiProductos.actualizarProducto(id, producto)
    }

    suspend fun eliminarProducto(id: Long): Response<Void> {
        return apiProductos.eliminarProducto(id)
    }

    suspend fun obtenerDivisas(base: String, destino: String): Response<MonedaApi> {
        return apiMonedas.obtenerCambio(base, destino)
    }
}