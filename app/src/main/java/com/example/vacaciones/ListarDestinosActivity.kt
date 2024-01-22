package com.example.vacaciones

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberImagePainter
import com.example.vacaciones.BBDD.DestinoDatabase
import com.example.vacaciones.Json.getMiIndicadorData
import com.example.vacaciones.modelos.Destino
import com.example.vacaciones.ui.theme.VacacionesTheme
import com.example.vacaciones.utils.getValorPago
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListarDestinosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VacacionesTheme {
                MostrarDestinos(lifecycleScope)
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MostrarDestinos(lifecycleScope: LifecycleCoroutineScope) {
    val context = LocalContext.current
    val destinoDatabase = DestinoDatabase.getInstance(context)
    var valorDolar by remember { mutableStateOf(0.0) }


    var destinos by remember { mutableStateOf(emptyList<Destino>()) }
    LaunchedEffect(key1 = destinos) {
        lifecycleScope.launch(Dispatchers.IO) {
            //Lista organizada por orden de visita y no por Id
            val destinosList = destinoDatabase.destinoDao().getAll().sortedBy { it.ordenVisita }
            withContext(Dispatchers.Main) {
                destinos = destinosList
            }
        }
        val datos = getMiIndicadorData()
        if (datos != null) {
            valorDolar = datos.dolar.valor
        }
    }

    // Mostrar la lista de destinos usando LazyColumn
    LazyColumn {
        items(destinos) { destino ->
            DestinoItem(destino = destino, lifecycleScope, valorDolar)
        }
    }
}

@Composable
fun DestinoItem(
    destino: Destino,
    lifecycleScope: LifecycleCoroutineScope,
    valor_dolar: Double) {

    val context = LocalContext.current
    val destinoDatabase = DestinoDatabase.getInstance(context)



    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Lado izquierdo: Imagen
        Image(
            painter = rememberImagePainter(data = destino.urlImagen),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(MaterialTheme.shapes.medium)
        )

        // Lado derecho: Información del destino y botones de acción
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        ) {
            Text(text = destino.nombre, fontWeight = FontWeight.Bold)
            Text(
                text = "${stringResource(id = R.string.costo_alojamiento_label)} ${getValorPago(
                    context, destino.costoAlojamiento, valor_dolar
                )}")
            Text(text = "${stringResource(id = R.string.costo_transporte_label)} ${
                destino.costoTransporte?.let {
                    getValorPago(
                        context, it, valor_dolar
                    )
                }
            }")
            Text(text = "${stringResource(id = R.string.orden_visita_label)} ${destino.ordenVisita}")


            // Botones de acción
            Row {
                IconButton(onClick = {
                    val intent = Intent(context, EditarDestinoActivity::class.java).apply {
                        putExtra("destino", destino)
                    }
                    context.startActivity(intent)
                }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = {
                    lifecycleScope.launch(Dispatchers.IO) {
                        destinoDatabase.destinoDao().delete(destino)
                    }
                    val intent = Intent(context, ListarDestinosActivity::class.java)
                    context.startActivity(intent)
                    Toast.makeText(
                        context,
                        "Se eliminó el destino ${destino.nombre}",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar")
                }
                Button(onClick = {
                    // Ir a la segunda actividad y pasar el objeto Destino
                    val intent = Intent(context, MostrarDestinoActivity::class.java).apply {
                        putExtra("destino", destino)
                    }
                    context.startActivity(intent)
                }) {
                    Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Ver más")
                    Text(stringResource(id = R.string.ver_mas_label))
                }
            }
        }
    }
}
