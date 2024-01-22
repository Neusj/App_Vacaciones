package com.example.vacaciones.BBDD

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.vacaciones.modelos.Destino

@Dao
interface DestinoDao {
    @Query("SELECT COUNT(*) FROM destino")
    fun count(): Int

    @Query("SELECT * FROM destino")
    fun getAll(): List<Destino>

    @Query("SELECT * FROM destino WHERE id = :id")
    fun findById(id: Long): Destino

    @Insert
    fun insert(destino: Destino): Long

    @Insert
    fun insertAll(vararg destinos: Destino)

    @Update
    fun update(vararg destinos: Destino)

    @Delete
    fun delete(destino: Destino)
}
