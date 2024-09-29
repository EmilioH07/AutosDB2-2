package com.erha.autosdb.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.erha.autosdb.data.db.model.AutoEntity
import com.erha.autosdb.util.Constants

@Dao
interface AutoDao {

    //Funciones para interactuar con la base de datos

    //Create
    @Insert
    suspend fun insertAuto(auto: AutoEntity)

    @Insert
    suspend fun insertGames(autos: MutableList<AutoEntity>)

    //Read
    @Query("SELECT * FROM ${Constants.DATABASE_GAME_TABLE}")
    suspend fun getAllAutos(): MutableList<AutoEntity>

    //Update
    @Update
    suspend fun updateAuto(auto: AutoEntity)

    //Delete
    @Delete
    suspend fun deleteAuto(auto: AutoEntity)
}