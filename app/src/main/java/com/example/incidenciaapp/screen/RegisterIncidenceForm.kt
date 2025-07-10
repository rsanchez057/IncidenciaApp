package com.example.incidenciaapp.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.incidenciaapp.model.IncidenciaDTO

@Composable
fun RegisterIncidenceForm(onSubmit: (IncidenciaDTO) -> Unit) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("PENDIENTE") }
    var tipo by remember { mutableStateOf("ACADÉMICA") }
    var cifAlumno by remember { mutableStateOf("") }
    var cifProfesor by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(24.dp)) {
        OutlinedTextField(value = titulo, onValueChange = { titulo = it }, label = { Text("Título") })
        OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") })
        OutlinedTextField(value = fecha, onValueChange = { fecha = it }, label = { Text("Fecha (yyyy-MM-dd)") })
        OutlinedTextField(value = estado, onValueChange = { estado = it }, label = { Text("Estado") })
        OutlinedTextField(value = tipo, onValueChange = { tipo = it }, label = { Text("Tipo") })
        OutlinedTextField(value = cifAlumno, onValueChange = { cifAlumno = it }, label = { Text("CIF Alumno") })
        OutlinedTextField(value = cifProfesor, onValueChange = { cifProfesor = it }, label = { Text("CIF Profesor") })

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val dto = IncidenciaDTO(
                id = null,
                titulo = titulo,
                descripcion = descripcion,
                fecha = fecha,
                estado = estado,
                tipo = tipo,
                cifAlumno = cifAlumno.toIntOrNull(),
                cifProfesor = cifProfesor.toIntOrNull(),
                nombreAlumno = null,
                nombreProfesor = null
            )
            onSubmit(dto)
        }) {
            Text("Registrar")
        }
    }
}
