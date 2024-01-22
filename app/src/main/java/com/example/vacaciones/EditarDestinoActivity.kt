package com.example.vacaciones

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.example.vacaciones.BBDD.DestinoDatabase
import com.example.vacaciones.modelos.Destino
import com.example.vacaciones.ui.theme.VacacionesTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditarDestinoActivity : ComponentActivity() {

    //private lateinit var viewModel: DestinoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VacacionesTheme {
                EditarDestinoScreen(
                    intent.getSerializableExtra("destino") as Destino,
                    lifecycleScope
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarDestinoScreen(destino: Destino, lifecycleScope: LifecycleCoroutineScope) {
    val context = LocalContext.current
    val destinoDatabase = DestinoDatabase.getInstance(context)
    var nombre by remember { mutableStateOf(destino.nombre) }
    var costoAlojamiento by remember { mutableStateOf(destino.costoAlojamiento.toString()) }
    var costoTransporte by remember { mutableStateOf(destino.costoTransporte.toString()) }
    var ordenVisita by remember { mutableStateOf(destino.ordenVisita.toString()) }
    var urlImagen by remember { mutableStateOf(destino.urlImagen) }
    var latitud by remember { mutableStateOf(destino.latitud.toString()) }
    var longitud by remember { mutableStateOf(destino.longitud.toString()) }
    var comentarios by remember { mutableStateOf(destino.comentarios.toString()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text(stringResource(id = R.string.nombre_lugar_label))},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        TextField(
            value = ordenVisita,
            onValueChange = { ordenVisita = it },
            label = { Text(stringResource(id = R.string.orden_visita_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        TextField(
            value = urlImagen,
            singleLine = true,
            onValueChange = { urlImagen = it },
            label = { Text(stringResource(id = R.string.foto_referencia_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        TextField(
            value = latitud,
            onValueChange = { latitud = it },
            label = { Text(stringResource(id = R.string.latitud_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        TextField(
            value = longitud,
            onValueChange = { longitud = it },
            label = { Text(stringResource(id = R.string.longitud_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        TextField(
            value = costoAlojamiento,
            onValueChange = { costoAlojamiento = it },
            label = { Text(stringResource(id = R.string.costo_alojamiento_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        TextField(
            value = costoTransporte,
            onValueChange = { costoTransporte = it },
            label = { Text(stringResource(id = R.string.costo_transporte_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        TextField(
            value = comentarios,
            onValueChange = { comentarios = it },
            label = { Text(stringResource(id = R.string.comentarios_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Button(
            onClick = {
                val editedDestino = destino.copy(
                    nombre = nombre,
                    urlImagen = urlImagen,
                    ordenVisita = ordenVisita.toInt(),
                    costoAlojamiento = costoAlojamiento.toInt(),
                    costoTransporte = costoTransporte.toInt(),
                    latitud = latitud.toDouble(),
                    longitud = longitud.toDouble(),
                    comentarios = comentarios
                )

                lifecycleScope.launch(Dispatchers.IO) {
                    destinoDatabase.destinoDao().update(editedDestino)
                }
                val intent = Intent(context, ListarDestinosActivity::class.java)
                context.startActivity(intent)
                Toast.makeText(
                    context,
                    "Se editó el destino ${destino.nombre}",
                    Toast.LENGTH_SHORT
                ).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(stringResource(id = R.string.guardar_label))
        }
    }
}
