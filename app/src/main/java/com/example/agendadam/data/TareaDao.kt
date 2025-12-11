package com.example.agendadam.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.agendadam.model.Tarea
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaDao {
    @Insert
    suspend fun insertar(tarea: Tarea)

    @Update
    suspend fun actualizar(tarea: Tarea)

    @Delete
    suspend fun eliminar(tarea: Tarea)

    @Query("SELECT * FROM tareas WHERE usuarioPropietario = :usuario ORDER BY id DESC")
    fun obtenerTareasPorUsuario(usuario: String): Flow<List<Tarea>>

    @Query("SELECT * FROM tareas WHERE usuarioPropietario = :usuario AND titulo LIKE '%' || :consulta || '%'")
    fun buscarTareas(usuario: String, consulta: String): Flow<List<Tarea>>
}