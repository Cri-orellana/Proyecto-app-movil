package com.tcg_project.repository

import com.tcg_project.model.ProductoApi
import com.tcg_project.model.TicketApi
import com.tcg_project.model.UsuarioApi
import com.tcg_project.network.ClienteRetrofit
import retrofit2.Response

class Repositorio {

    private val api = ClienteRetrofit.servicioApi

    suspend fun obtenerTickets(): Response<List<TicketApi>> {
        return api.obtenerTodosLosTickets()
    }

    suspend fun crearTicket(ticket: TicketApi): Response<TicketApi> {
        return api.crearTicket(ticket)
    }
    suspend fun eliminarTicket(id: Int): Response<Void> {
        return api.eliminarTicket(id)
    }

    suspend fun crearUsuario(usuario: UsuarioApi): Response<UsuarioApi> {
        return api.crearUsuario(usuario)
    }

    suspend fun obtenerUsuarios(): Response<List<UsuarioApi>> {
        return api.obtenerTodosLosUsuarios()
    }

    suspend fun obtenerProductos(): Response<List<ProductoApi>> {
        return api.obtenerTodosLosProductos()
    }

}