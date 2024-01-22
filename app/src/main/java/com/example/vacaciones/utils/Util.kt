package com.example.vacaciones.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.vacaciones.BBDD.DestinoDatabase
import com.example.vacaciones.ListarDestinosActivity
import com.example.vacaciones.R
import com.example.vacaciones.modelos.Destino
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun DestinoForm(lifecycleScope: LifecycleCoroutineScope, btnText: String) {
    var nombre by remember { mutableStateOf("") }
    var ordenVisita by remember { mutableStateOf("") }
    var urlImagen by remember { mutableStateOf("") }
    var latitud by remember { mutableStateOf("") }
    var longitud by remember { mutableStateOf("") }
    var costoAlojamiento by remember { mutableStateOf("") }
    var costoTransporte by remember { mutableStateOf("") }
    var comentarios by remember { mutableStateOf("") }

    val context = LocalContext.current
    val destinoDatabase = DestinoDatabase.getInstance(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Formulario de ingreso
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text(stringResource(id = R.string.nombre_lugar_label)) },
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

        // Botón de guardado
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    // Crear el objeto Destino
                    val destino = Destino(
                        nombre = nombre,
                        ordenVisita = ordenVisita.toIntOrNull() ?: 0,
                        urlImagen = urlImagen,
                        latitud = latitud.toDoubleOrNull() ?: 0.0,
                        longitud = longitud.toDoubleOrNull() ?: 0.0,
                        costoAlojamiento = costoAlojamiento.toIntOrNull() ?: 0,
                        costoTransporte = costoTransporte.toIntOrNull() ?: 0,
                        comentarios = comentarios
                    )

                    lifecycleScope.launch(Dispatchers.IO) {
                        destinoDatabase.destinoDao().insert(destino)
                    }
                    val intent = Intent(context, ListarDestinosActivity::class.java)
                    context.startActivity(intent)
                    Toast.makeText(
                        context,
                        "Se guardó el destino exitosamente",
                        Toast.LENGTH_SHORT
                    ).show()

                },
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(stringResource(id = R.string.guardar_label))
                Spacer(modifier = Modifier.width(8.dp))

            }
            Button(
                onClick = {
                    val intent = Intent(context, ListarDestinosActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(id = R.string.listar_label))
            }

        }
    }
}


fun getValorPago(context: Context, pesos: Int, valorDolar: Double): String {

    if (context.resources.configuration.locale.language == "en"){
        var valor = ((pesos * valorDolar) / 1000000)
        return "USD ${limitarDecimales(valor)}"
    } else {
        return "CLP $pesos"
    }
}

fun limitarDecimales(valor: Double): String {
    val formato = DecimalFormat("#.##")
    return formato.format(valor)
}