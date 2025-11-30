package com.example.movil_tcg_app.api

import com.example.movil_tcg_app.model.UsuarioApi
import com.example.movil_tcg_app.network.UsuarioService
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

class UsuarioApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var usuarioService: UsuarioService
    private val gson = Gson()

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        usuarioService = retrofit.create(UsuarioService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    // --- TESTS DE ÉXITO ---

    @Test
    fun `obtenerTodosLosUsuarios_ListaUsuarios`() = runTest {
        val usuariosFalsos = listOf(
            UsuarioApi(1, "Usuario Uno", "user1@test.com", "pass1", true),
            UsuarioApi(2, "Usuario Dos", "user2@test.com", "pass2", true)
        )
        val mockResponse = MockResponse().setResponseCode(200).setBody(gson.toJson(usuariosFalsos))
        mockWebServer.enqueue(mockResponse)

        val response = usuarioService.obtenerTodosLosUsuarios()

        assertTrue(response.isSuccessful)
        assertEquals(2, response.body()?.size)
    }

    @Test
    fun `obtenerUsuarioPorId_devuelveUsuario`() = runTest {
        val usuarioFalso = UsuarioApi(1, "Usuario Uno", "user1@test.com", "pass1", true)
        val mockResponse = MockResponse().setResponseCode(200).setBody(gson.toJson(usuarioFalso))
        mockWebServer.enqueue(mockResponse)

        val response = usuarioService.obtenerUsuarioPorId(1)

        assertTrue(response.isSuccessful)
        assertEquals("Usuario Uno", response.body()?.usuarioNombre)
    }

    @Test
    fun `crearUsuario_devuelveUsuarioCreado`() = runTest {
        val nuevoUsuario = UsuarioApi(null, "Usuario Tres", "user3@test.com", "pass3", true)
        val usuarioCreado = nuevoUsuario.copy(id = 3)
        val mockResponse = MockResponse().setResponseCode(201).setBody(gson.toJson(usuarioCreado))
        mockWebServer.enqueue(mockResponse)

        val response = usuarioService.crearUsuario(nuevoUsuario)

        assertTrue(response.isSuccessful)
        assertEquals(3, response.body()?.id)
    }

    @Test
    fun `actualizarUsuario_Actualizado`() = runTest {
        val usuarioActualizado = UsuarioApi(1, "Nombre Actualizado", "user1@test.com", "newpass",true)
        val mockResponse = MockResponse().setResponseCode(200).setBody(gson.toJson(usuarioActualizado))
        mockWebServer.enqueue(mockResponse)

        val response = usuarioService.actualizarUsuario(1, usuarioActualizado)

        assertTrue(response.isSuccessful)
        assertEquals("Nombre Actualizado", response.body()?.usuarioNombre)
    }

    @Test
    fun `eliminarUsuario_Eliminado`() = runTest {
        val mockResponse = MockResponse().setResponseCode(204) // No Content
        mockWebServer.enqueue(mockResponse)

        val response = usuarioService.eliminarUsuario(1)

        assertTrue(response.isSuccessful)
    }

    // --- TESTS DE ERRORES ---

    @Test
    fun `obtenerTodosLosUsuarios_devuelveError`() = runTest {
        val mockResponse = MockResponse().setResponseCode(500)
        mockWebServer.enqueue(mockResponse)

        val response = usuarioService.obtenerTodosLosUsuarios()

        assertFalse(response.isSuccessful)
        assertEquals(500, response.code())
    }

    @Test
    fun `obtenerUsuarioPorId_noEncontrado`() = runTest {
        val mockResponse = MockResponse().setResponseCode(404)
        mockWebServer.enqueue(mockResponse)

        val response = usuarioService.obtenerUsuarioPorId(999)

        assertFalse(response.isSuccessful)
        assertEquals(404, response.code())
    }

    @Test
    fun `crearUsuario_errorSiTerminosSonFalsos`() = runTest {
        val usuarioSinTerminos = UsuarioApi(null, "Test", "test@test.com", "123", false)
        val mockResponse = MockResponse().setResponseCode(400) // Bad Request
        mockWebServer.enqueue(mockResponse)

        val response = usuarioService.crearUsuario(usuarioSinTerminos)

        assertFalse(response.isSuccessful)
        assertEquals(400, response.code())
    }

    @Test
    fun `actualizarUsuario_NoEncontrado`() = runTest {
        val mockResponse = MockResponse().setResponseCode(404)
        mockWebServer.enqueue(mockResponse)

        val usuario = UsuarioApi(999, "Inexistente", "n@a.com", "-",true)
        val response = usuarioService.actualizarUsuario(999, usuario)

        assertFalse(response.isSuccessful)
        assertEquals(404, response.code())
    }

    @Test
    fun `eliminarUsuario_NoEncontrado`() = runTest {
        val mockResponse = MockResponse().setResponseCode(404)
        mockWebServer.enqueue(mockResponse)

        val response = usuarioService.eliminarUsuario(999)

        assertFalse(response.isSuccessful)
        assertEquals(404, response.code())
    }
}