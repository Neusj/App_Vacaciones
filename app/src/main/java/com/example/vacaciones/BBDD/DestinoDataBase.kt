package com.example.vacaciones.BBDD

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.vacaciones.modelos.Destino


@Database(entities = [Destino::class], version = 1)
abstract class DestinoDatabase : RoomDatabase() {
    abstract fun destinoDao(): DestinoDao

    companion object {
        @Volatile
        private var INSTANCE: DestinoDatabase? = null

        fun getInstance(context: Context): DestinoDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    DestinoDatabase::class.java,
                    "Destino.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}