package com.example.agendadam.data

import android.content.Context

class Prefs(val context: Context) {
    val SHARED_DB_NAME = "MyDatabase"
    val SHARED_VIP = "vip_user"

    val storage = context.getSharedPreferences(SHARED_DB_NAME, 0)

    fun saveVIP(isVip: Boolean) {
        storage.edit().putBoolean(SHARED_VIP, isVip).apply()
    }

    fun getVIP(): Boolean {
        return storage.getBoolean(SHARED_VIP, false) // false por defecto
    }

    fun wipe() {
        storage.edit().clear().apply()
    }
}