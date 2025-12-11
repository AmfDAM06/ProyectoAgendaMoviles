package com.example.agendadam

import android.app.Application
import com.example.agendadam.data.AgendaDatabase
import com.example.agendadam.data.Prefs

class AgendaApp : Application() {

    val database by lazy { AgendaDatabase.getDatabase(this) }

    companion object {
        lateinit var prefs: Prefs
    }

    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(applicationContext)
    }
}