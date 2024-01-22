package com.example.vacaciones.fotos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.vacaciones.modelos.Destino

class PhotosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TomarPhoto(intent.getSerializableExtra("destino") as Destino,)
        }
    }
}