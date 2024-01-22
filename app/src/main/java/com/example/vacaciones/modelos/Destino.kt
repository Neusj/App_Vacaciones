package com.example.vacaciones.modelos

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity
data class Destino(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val ordenVisita: Int,
    val urlImagen: String,
    val uri_Imagen: String? = null,
    val latitud: Double,
    val longitud: Double,
    val costoAlojamiento: Int,
    val costoTransporte: Int? = 0,
    val comentarios: String
): Serializable
