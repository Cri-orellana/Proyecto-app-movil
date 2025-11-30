package com.example.movil_tcg_app.api

import com.example.movil_tcg_app.model.ProductoApi
import com.example.movil_tcg_app.network.ProductoService
import com.google.gson.Gson
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductoApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var productoService: ProductoService
    private val gson = Gson()

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        productoService = retrofit.create(ProductoService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `obtenerTodosLosProductos_devuelveLista`() = runTest {
        val productosFalsos = listOf(
            ProductoApi(1, "Yugioh", "Carta", "Mago Oscuro", 1000, "El mago definitivo.", "url1"),
            ProductoApi(2, "Pokemon", "Carta", "Charizard", 2500, "La evolución de Charmander.", "url2")
        )
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(productosFalsos))
        mockWebServer.enqueue(mockResponse)

        val response = productoService.obtenerTodosLosProductos()

        assertTrue(response.isSuccessful)
        assertEquals(2, response.body()?.size)
    }

    @Test
    fun `obtenerProductoPorId_devuelveProducto`() = runTest {
        val productoFalso = ProductoApi(1, "Yugioh", "Carta", "Mago Oscuro", 1000, "El mago definitivo.", "url1")
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(productoFalso))
        mockWebServer.enqueue(mockResponse)

        val response = productoService.obtenerProductoPorId(1L)

        assertTrue(response.isSuccessful)
        assertEquals("Mago Oscuro", response.body()?.nombreProduto)
    }

    @Test
    fun `crearProducto_SeCreaProducto`() = runTest {
        val nuevoProducto = ProductoApi(null, "Magic", "Carta", "Black Lotus", 5000, "Una carta muy poderosa.", "url3")
        val productoCreado = nuevoProducto.copy(productId = 3)
        val mockResponse = MockResponse().setResponseCode(201).setBody(gson.toJson(productoCreado))
        mockWebServer.enqueue(mockResponse)

        val response = productoService.crearProducto(nuevoProducto)

        assertTrue(response.isSuccessful)
        assertEquals(3L, response.body()?.productId)
    }

    @Test
    fun `actualizarProducto_SeActualizaProducto`() = runTest {
        val productoActualizado = ProductoApi(1, "Yugioh", "Carta", "Mago Oscuro Actualizado", 1200, "Descripción actualizada.", "url1_updated")
        val mockResponse = MockResponse().setResponseCode(200).setBody(gson.toJson(productoActualizado))
        mockWebServer.enqueue(mockResponse)

        val response = productoService.actualizarProducto(1L, productoActualizado)

        assertTrue(response.isSuccessful)
        assertEquals("Mago Oscuro Actualizado", response.body()?.nombreProduto)
    }

    @Test
    fun `eliminarProducto_SeEliminaProducto`() = runTest {
        val mockResponse = MockResponse().setResponseCode(204)
        mockWebServer.enqueue(mockResponse)

        val response = productoService.eliminarProducto(1L)

        assertTrue(response.isSuccessful)
    }

    // --- TESTS DE ERRORES ---

    @Test
    fun `obtenerTodosLosProductos_ErrorDelServidor`() = runTest {
        val mockResponse = MockResponse().setResponseCode(500) // Simula un error del servidor
        mockWebServer.enqueue(mockResponse)

        val response = productoService.obtenerTodosLosProductos()

        assertFalse(response.isSuccessful)
        assertEquals(500, response.code())
    }

    @Test
    fun `obtenerProductoPorId_NoEncontrado`() = runTest {
        val mockResponse = MockResponse().setResponseCode(404)
        mockWebServer.enqueue(mockResponse)

        val response = productoService.obtenerProductoPorId(999L)

        assertFalse(response.isSuccessful)
        assertEquals(404, response.code())
    }

    @Test
    fun `crearProducto_CreacionIncorrecta`() = runTest {
        val mockResponse = MockResponse().setResponseCode(400) // Bad Request
        mockWebServer.enqueue(mockResponse)

        val productoInvalido = ProductoApi(null, "", "", "", 0, "", "") // Producto con datos inválidos
        val response = productoService.crearProducto(productoInvalido)

        assertFalse(response.isSuccessful)
        assertEquals(400, response.code())
    }

    @Test
    fun `actualizarProducto_NoExiste`() = runTest {
        val mockResponse = MockResponse().setResponseCode(404) // Not Found
        mockWebServer.enqueue(mockResponse)

        val producto = ProductoApi(999, "Yugioh", "Carta", "Inexistente", 0, "", "")
        val response = productoService.actualizarProducto(999L, producto)

        assertFalse(response.isSuccessful)
        assertEquals(404, response.code())
    }

    @Test
    fun `eliminarProducto_NoEncontrado`() = runTest {
        val mockResponse = MockResponse().setResponseCode(404)
        mockWebServer.enqueue(mockResponse)

        val response = productoService.eliminarProducto(999L)

        assertFalse(response.isSuccessful)
        assertEquals(404, response.code())
    }
}