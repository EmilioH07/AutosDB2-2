package com.erha.autosdb.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.erha.autosdb.data.db.model.AutoEntity
import com.erha.autosdb.util.Constants

@Database(
    entities = [AutoEntity::class],
    version = 1, //versión de la bd para migraciones
    exportSchema = true //por defecto es true.
)

abstract class AutoDatabase : RoomDatabase()  {

    //Aquí va el DAO
    abstract fun gameDao(): AutoDao

    //Sin inyección de dependencias, instanciamos la base de datos
    //aquí con un patrón singleton

    companion object {

        @Volatile
        private var INSTANCE: AutoDatabase? = null

        fun getDatabase(context: Context): AutoDatabase {
            //Si la instancia no es nula, entonces vamos
            //a regresar la que ya tenemos
            //Si es nula, creamos una instancia y la regresamos
            //(patrón singleton)

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AutoDatabase::class.java,
                    Constants.DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                instance
            }

        }

    }
}