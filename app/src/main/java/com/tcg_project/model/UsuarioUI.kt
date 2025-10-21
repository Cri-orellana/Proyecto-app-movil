package com.tcg_project.model

data class UsuarioUI(val email: String = "",
                     val password: String = "",
                     val nombre: String = "",
                     val direccion: String = "",
                     val aceptaTerminos: Boolean = false,
                     val errores: UsuarioErrores = UsuarioErrores()
)
