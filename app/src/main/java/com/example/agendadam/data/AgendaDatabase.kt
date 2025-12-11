package com.example.agendadam.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.agendadam.model.Tarea
import com.example.agendadam.model.Usuario

@Database(entities = [Usuario::class, Tarea::class], version = 2, exportSchema = false)
abstract class AgendaDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    abstract fun tareaDao(): TareaDao

    companion object {
        @Volatile
        private var INSTANCE: AgendaDatabase? = null

        fun getDatabase(context: Context): AgendaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AgendaDatabase::class.java,
                    "agenda_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}