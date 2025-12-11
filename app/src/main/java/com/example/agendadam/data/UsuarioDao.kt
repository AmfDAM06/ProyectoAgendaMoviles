package com.example.agendadam.data // Tu paquete

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.agendadam.model.Usuario

@Dao
interface UsuarioDao {

    @Update
    suspend fun actualizarUsuario(usuario: Usuario)
    @Insert
    suspend fun insertarUsuario(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE nombreUsuario = :nombre AND contrasena = :pass LIMIT 1")
    suspend fun login(nombre: String, pass: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE nombreUsuario = :nombre LIMIT 1")
    suspend fun buscarPorNombre(nombre: String): Usuario?
}