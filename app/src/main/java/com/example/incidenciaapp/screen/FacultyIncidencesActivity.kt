package com.example.incidenciaapp.screen

import android.os.Bundle
import androidx.compose.ui.platform.LocalContext
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.incidenciaapp.model.IncidenciaDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.ui.Alignment

class FacultyIncidencesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FacultyIncidencesScreen()
        }
    }
}


@Composable
fun FacultyIncidencesScreen() {
    val context = LocalContext.current
    var incidencias by remember { mutableStateOf<List<IncidenciaDTO>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(true) {
        RetrofitInstance.getAuthApi(context).getIncidenciasDeFacultad().enqueue(object : Callback<List<IncidenciaDTO>> {
            override fun onResponse(call: Call<List<IncidenciaDTO>>, response: Response<List<IncidenciaDTO>>) {
                if (response.isSuccessful) {
                    incidencias = response.body() ?: emptyList()
                }
                isLoading = false
            }

            override fun onFailure(call: Call<List<IncidenciaDTO>>, t: Throwable) {
                isLoading = false
            }
        })
    }


    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(incidencias.size) { index ->
                val incidencia = incidencias[index]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Título: ${incidencia.titulo}", style = MaterialTheme.typography.titleMedium)
                        Text("Descripción: ${incidencia.descripcion}")
                        Text("Fecha: ${incidencia.fecha}")
                        Text("Estado: ${incidencia.estado}")
                        Text("Tipo: ${incidencia.tipo}")
                        Text("Alumno: ${incidencia.nombreAlumno ?: "-"}")
                        Text("Profesor: ${incidencia.nombreProfesor ?: "-"}")
                    }
                }
            }
        }
    }
}
