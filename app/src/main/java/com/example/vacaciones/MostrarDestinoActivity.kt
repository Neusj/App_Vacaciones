package com.example.vacaciones

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.example.vacaciones.modelos.Destino
import com.example.vacaciones.ui.theme.VacacionesTheme
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.vacaciones.BBDD.DestinoDatabase
import com.example.vacaciones.Json.getMiIndicadorData
import com.example.vacaciones.fotos.PhotosActivity
import com.example.vacaciones.mapa.MyMapa
import com.example.vacaciones.utils.getValorPago
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MostrarDestinoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VacacionesTheme {
                MostrarDestino(intent, lifecycleScope)
            }
        }
    }
}

@Composable
fun MostrarDestino(intent: android.content.Intent, lifecycleScope: LifecycleCoroutineScope) {
    val destino = intent.getSerializableExtra("destino") as? Destino
    val context = LocalContext.current
    val destinoDatabase = DestinoDatabase.getInstance(context)

    var valorDolar by remember { mutableStateOf(0.0) }
    val datos = getMiIndicadorData()
    if (datos != null) {
        valorDolar = datos.dolar.valor
    }


    destino?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título centrado y en negrita
            Text(
                text = "${destino.nombre}",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(8.dp)
            )

            // Imagen centrada y de tamaño mediano
            Image(
                painter = rememberImagePainter(
                    data = destino.urlImagen,
                    builder = {
                        transformations(CircleCropTransformation())
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(250.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(8.dp),
                contentScale = ContentScale.Crop
            )

            // Lugar y Transporte
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                // Lugar
                Text(
                    text = stringResource(id = R.string.alojamiento_label),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                )
                Text(
                    "${getValorPago(context, destino.costoAlojamiento, valorDolar)}",
                    modifier = Modifier.weight(1f)
                )

                // Transporte
                Text(
                    stringResource(id = R.string.transporte_label),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                )
                Text(
                    "${destino.costoTransporte?.let { it1 ->
                        getValorPago(context,
                            it1, valorDolar)
                    }}",
                    modifier = Modifier.weight(1f)
                )
            }

            // Comentarios
            Text(
                stringResource(id = R.string.comentarios_label),
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),

            )
            Text(text = "${destino.comentarios}", modifier = Modifier.padding(8.dp))

            // Botones de iconos
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(id = R.string.foto_label), modifier = Modifier.padding(8.dp))
                IconButton(onClick = {
                    val intent = Intent(context, PhotosActivity::class.java).apply {
                        putExtra("destino", destino)
                    }
                    context.startActivity(intent)
                }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Foto")
                }

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
            }

            MyMapa(destino)

        }
    }
}
