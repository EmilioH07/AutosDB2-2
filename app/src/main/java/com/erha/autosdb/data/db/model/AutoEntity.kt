package com.erha.autosdb.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.erha.autosdb.util.Constants

@Entity(tableName = Constants.DATABASE_GAME_TABLE)
data class AutoEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "game_id")
    var id: Long = 0,
    @ColumnInfo(name = "game_title")
    var marca: String,
    @ColumnInfo(name = "game_genre")
    var modelo: String,
    @ColumnInfo(name = "game_developer", defaultValue = "Desconocido")
    var colors: String
)