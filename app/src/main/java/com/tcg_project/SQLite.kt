package com.tcg_project

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Producto(val id: String, val descripcion: String, val precio: Int, val url: String, val franquicia: String)
data class CarritoItem(val productoId: String, val usuarioEmail: String, var cantidad: Int)
data class Usuario(val id: Int, val nombre: String, val email: String, val direccion: String)

class SQLite private constructor(
    context: Context?,
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 5 // Versión incrementada
        private const val DATABASE_NAME = "tcg_database.db"

        @Volatile
        private var INSTANCE: SQLite? = null

        fun getInstance(context: Context): SQLite {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SQLite(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE productos (id TEXT PRIMARY KEY, descripcion TEXT, precio INTEGER, url TEXT, franquicia TEXT)")
        db?.execSQL("CREATE TABLE usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, email TEXT UNIQUE, password TEXT, direccion TEXT)")
        db?.execSQL("CREATE TABLE carrito (producto_id TEXT, usuario_email TEXT, cantidad INTEGER, PRIMARY KEY (producto_id, usuario_email))")

        insertInitialData(db)
    }

    private fun insertInitialData(db: SQLiteDatabase?) {
        // Yugioh
        db?.execSQL("INSERT INTO productos (id, descripcion, precio, url, franquicia) VALUES ('ygo-001', 'Sobre de 24 cartas de Yugioh', 5990, 'https://storage.googleapis.com/ygoprodeck.com/pics_artgame/25774450.jpg', 'Yugioh')")
        db?.execSQL("INSERT INTO productos (id, descripcion, precio, url, franquicia) VALUES ('ygo-002', 'Caja de 24 sobres de Yugioh', 120000, 'https://m.media-amazon.com/images/I/71T58n5GfIL._AC_UF894,1000_QL80_.jpg', 'Yugioh')")
        db?.execSQL("INSERT INTO productos (id, descripcion, precio, url, franquicia) VALUES ('ygo-003', 'Deck Estructural Yugi Muto', 14990, 'https://www.inquest.cl/wp-content/uploads/2021/08/Structure-Deck-Spirit-Charmers-Yu-Gi-Oh-TCG-Inquest.jpg', 'Yugioh')")
        // Magic
        db?.execSQL("INSERT INTO productos (id, descripcion, precio, url, franquicia) VALUES ('mtg-001', 'Sobre de 24 cartas de Magic', 4990, 'https://www.inquest.cl/wp-content/uploads/2023/10/4-sobres-play-booster-las-cavernas-perdidas-de-ixalan-magic-the-gathering-preventa.jpg', 'Magic')")
        db?.execSQL("INSERT INTO productos (id, descripcion, precio, url, franquicia) VALUES ('mtg-002', 'Kit de construcción de mazos', 24990, 'https://d2j6dbq0eux0bg.cloudfront.net/images/41372134/2331577977.jpg', 'Magic')")
        db?.execSQL("INSERT INTO productos (id, descripcion, precio, url, franquicia) VALUES ('mtg-003', 'Mazo de Commander', 39990, 'https://www.magicsur.cl/108985-large_default/mazo-de-commander-de-modern-horizons-3-creative-energy-ingles.jpg', 'Magic')")
        // Pokemon
        db?.execSQL("INSERT INTO productos (id, descripcion, precio, url, franquicia) VALUES ('pktcg-001', 'Sobre de 24 cartas de Pokemon', 6990, 'https://cf.shopee.cl/file/sg-11134201-22110-g3u1kth7e1jv7b', 'Pokemon')")
        db?.execSQL("INSERT INTO productos (id, descripcion, precio, url, franquicia) VALUES ('pktcg-002', 'Caja de Entrenador Élite', 49990, 'https://www.weplay.cl/pub/media/catalog/product/cache/44c805256e22bd938096a6ac4662f275/p/o/pokemon_tcg_swsh11_lost_origin_elite_trainer_box_1.jpg', 'Pokemon')")
        db?.execSQL("INSERT INTO productos (id, descripcion, precio, url, franquicia) VALUES ('pktcg-003', 'Lata de coleccionista', 29990, 'https://www.lahuelladeporte.cl/wp-content/uploads/2023/07/Lata-Pokemon-Escarlata-y-Purpura-Paldea-Friends-2-1.jpg', 'Pokemon')")
        // Mitos y Leyendas
        db?.execSQL("INSERT INTO productos (id, descripcion, precio, url, franquicia) VALUES ('myl-001', 'Sobre de 11 cartas de Mitos y Leyendas', 1990, 'https://www.lahuelladeporte.cl/wp-content/uploads/2023/04/Draconis-Wolk-2.jpg', 'Mitos y Leyendas')")
        db?.execSQL("INSERT INTO productos (id, descripcion, precio, url, franquicia) VALUES ('myl-002', 'Colección legendaria', 21990, 'https://www.devir.cl/wp-content/uploads/2022/10/producto_myl_dks.png', 'Mitos y Leyendas')")
        db?.execSQL("INSERT INTO productos (id, descripcion, precio, url, franquicia) VALUES ('myl-003', 'Mazo preconstruido', 9990, 'https://salojuegos.cl/wp-content/uploads/2023/11/mazo-keltoi.jpg', 'Mitos y Leyendas')")
        
        // Usuario de ejemplo
        db?.execSQL("INSERT INTO usuarios (nombre, email, password, direccion) VALUES ('usuario', 'ejemplo@gmail.com', '123456', 'callefalsa123')")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Para desarrollo, la forma más fácil es borrar y recrear.
        db?.execSQL("DROP TABLE IF EXISTS productos")
        db?.execSQL("DROP TABLE IF EXISTS usuarios")
        db?.execSQL("DROP TABLE IF EXISTS carrito")
        onCreate(db)
    }

    @SuppressLint("Range")
    fun getUsuarioPorEmail(email: String): Usuario? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM usuarios WHERE email = ?", arrayOf(email))
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val nombre = cursor.getString(cursor.getColumnIndex("nombre"))
            val direccion = cursor.getString(cursor.getColumnIndex("direccion"))
            cursor.close()
            // Usamos el email de la consulta para asegurar consistencia
            return Usuario(id, nombre, email, direccion)
        }
        cursor.close()
        return null
    }

    fun registrarUsuario(nombre: String, email: String, pass: String, direccion: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("nombre", nombre)
            put("email", email)
            put("password", pass)
            put("direccion", direccion)
        }
        return db.insert("usuarios", null, contentValues)
    }

    fun loginUsuario(email: String, pass: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM usuarios WHERE email = ? AND password = ?"
        val cursor = db.rawQuery(query, arrayOf(email, pass))
        val userExists = cursor.count > 0
        cursor.close()
        return userExists
    }

    fun agregarAlCarrito(item: CarritoItem) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("producto_id", item.productoId)
            put("usuario_email", item.usuarioEmail)
            put("cantidad", item.cantidad)
        }
        db.insertWithOnConflict("carrito", null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }

    @SuppressLint("Range")
    fun obtenerItemsDelCarrito(usuarioEmail: String): List<CarritoItem> {
        val items = mutableListOf<CarritoItem>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM carrito WHERE usuario_email = ?", arrayOf(usuarioEmail))
        while (cursor.moveToNext()) {
            val productoId = cursor.getString(cursor.getColumnIndex("producto_id"))
            val cantidad = cursor.getInt(cursor.getColumnIndex("cantidad"))
            items.add(CarritoItem(productoId, usuarioEmail, cantidad))
        }
        cursor.close()
        return items
    }

    fun eliminarItemDelCarrito(productoId: String, usuarioEmail: String) {
        val db = this.writableDatabase
        db.delete("carrito", "producto_id = ? AND usuario_email = ?", arrayOf(productoId, usuarioEmail))
    }

    @SuppressLint("Range")
    fun getProductos(): ArrayList<Producto> {
        val productos = ArrayList<Producto>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM productos", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getString(cursor.getColumnIndex("id"))
                val descripcion = cursor.getString(cursor.getColumnIndex("descripcion"))
                val precio = cursor.getInt(cursor.getColumnIndex("precio"))
                val url = cursor.getString(cursor.getColumnIndex("url"))
                val franquicia = cursor.getString(cursor.getColumnIndex("franquicia"))
                productos.add(Producto(id, descripcion, precio, url, franquicia))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return productos
    }

    @SuppressLint("Range")
    fun getArticulosDestacados(): ArrayList<Producto> {
        val productos = ArrayList<Producto>()
        val db = this.readableDatabase
        // Esta consulta obtiene el primer producto que encuentra para cada franquicia.
        val cursor = db.rawQuery("SELECT * FROM productos GROUP BY franquicia", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getString(cursor.getColumnIndex("id"))
                val descripcion = cursor.getString(cursor.getColumnIndex("descripcion"))
                val precio = cursor.getInt(cursor.getColumnIndex("precio"))
                val url = cursor.getString(cursor.getColumnIndex("url"))
                val franquicia = cursor.getString(cursor.getColumnIndex("franquicia"))
                productos.add(Producto(id, descripcion, precio, url, franquicia))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return productos
    }
}