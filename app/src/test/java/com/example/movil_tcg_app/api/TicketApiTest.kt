package com.example.movil_tcg_app.api

import com.example.movil_tcg_app.model.TicketApi
import com.example.movil_tcg_app.network.TicketService
import com.google.gson.Gson
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TicketApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var ticketService: TicketService
    private val gson = Gson()

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        ticketService = retrofit.create(TicketService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    // --- TESTS DE ÉXITO ---

    @Test
    fun `obtenerTodosLosTickets_ListaTickets`() = runTest {
        val ticketsFalsos = listOf(
            TicketApi(1, "Usuario Uno", "user1@test.com", "Problema con el pedido", "No ha llegado.", "ABIERTO"),
            TicketApi(2, "Usuario Dos", "user2@test.com", "Duda sobre un producto", "¿Es compatible?", "ABIERTO")
        )
        val mockResponse = MockResponse().setResponseCode(200).setBody(gson.toJson(ticketsFalsos))
        mockWebServer.enqueue(mockResponse)

        val response = ticketService.obtenerTodosLosTickets()

        assertTrue(response.isSuccessful)
        assertEquals(2, response.body()?.size)
    }

    @Test
    fun `crearTicket_SeCreaTicket`() = runTest {
        val nuevoTicket = TicketApi(null, "Usuario Tres", "user3@test.com", "Sugerencia", "Añadir más productos.")
        val ticketCreado = nuevoTicket.copy(id = 3)
        val mockResponse = MockResponse().setResponseCode(201).setBody(gson.toJson(ticketCreado))
        mockWebServer.enqueue(mockResponse)

        val response = ticketService.crearTicket(nuevoTicket)

        assertTrue(response.isSuccessful)
        assertEquals(3, response.body()?.id)
    }

    @Test
    fun `actualizarTicket_SeActualizaTicket`() = runTest {
        val ticketActualizado = TicketApi(1, "Usuario Uno", "user1@test.com", "Problema con el pedido", "Ya ha llegado.", "CERRADO")
        val mockResponse = MockResponse().setResponseCode(200).setBody(gson.toJson(ticketActualizado))
        mockWebServer.enqueue(mockResponse)

        val response = ticketService.actualizarTicket(1, ticketActualizado)

        assertTrue(response.isSuccessful)
        assertEquals("CERRADO", response.body()?.estado)
    }

    @Test
    fun `eliminarTicket_SeEliminaTicket`() = runTest {
        val mockResponse = MockResponse().setResponseCode(204)
        mockWebServer.enqueue(mockResponse)

        val response = ticketService.eliminarTicket(1)

        assertTrue(response.isSuccessful)
    }

    // --- TESTS DE ERRORES ---

    @Test
    fun `obtenerTodosLosTickets_ErrorServidor`() = runTest {
        val mockResponse = MockResponse().setResponseCode(500)
        mockWebServer.enqueue(mockResponse)

        val response = ticketService.obtenerTodosLosTickets()

        assertFalse(response.isSuccessful)
        assertEquals(500, response.code())
    }

    @Test
    fun `obtenerTicketPorId_TicketNoEncontrado`() = runTest {
        val mockResponse = MockResponse().setResponseCode(404)
        mockWebServer.enqueue(mockResponse)

        val response = ticketService.obtenerTicketPorId(999)

        assertFalse(response.isSuccessful)
        assertEquals(404, response.code())
    }
    
    @Test
    fun `actualizarTicket_TicketNoEncontrado`() = runTest {
        val mockResponse = MockResponse().setResponseCode(404)
        mockWebServer.enqueue(mockResponse)

        val ticket = TicketApi(999, "Inexistente", "n@a.com", "", "")
        val response = ticketService.actualizarTicket(999, ticket)

        assertFalse(response.isSuccessful)
        assertEquals(404, response.code())
    }

    @Test
    fun `eliminarTicket_TicketNoEncontrado`() = runTest {
        val mockResponse = MockResponse().setResponseCode(404)
        mockWebServer.enqueue(mockResponse)

        val response = ticketService.eliminarTicket(999)

        assertFalse(response.isSuccessful)
        assertEquals(404, response.code())
    }
}