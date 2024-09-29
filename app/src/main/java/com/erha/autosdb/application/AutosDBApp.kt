package com.erha.autosdb.application

import android.app.Application
import com.erha.autosdb.data.AutoRepository
import com.erha.autosdb.data.db.AutoDatabase

class AutosDBApp: Application() {
    private val database by lazy{
        AutoDatabase.getDatabase(this@AutosDBApp)
    }

    val repository by lazy {
        AutoRepository(database.gameDao())
    }
}