package com.tcg_project.repository

import com.tcg_project.model.ProductoApi
import com.tcg_project.model.TicketApi
import com.tcg_project.model.UsuarioApi
import com.tcg_project.network.ClienteRetrofit
import retrofit2.Response

class Repositorio {

    private val apiTickets = ClienteRetrofit.ticketService
    private val apiProductos = ClienteRetrofit.productoService
    private val apiUsuarios = ClienteRetrofit.usuarioService

    suspend fun obtenerTickets(): Response<List<TicketApi>> {
        return apiTickets.obtenerTodosLosTickets()
    }

    suspend fun crearTicket(ticket: TicketApi): Response<TicketApi> {
        return apiTickets.crearTicket(ticket)
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
}