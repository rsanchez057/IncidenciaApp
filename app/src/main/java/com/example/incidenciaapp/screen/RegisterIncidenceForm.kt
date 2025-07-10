package com.example.incidenciaapp.screen

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.incidenciaapp.model.IncidenciaDTO
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RegisterIncidenceForm(onSubmit: (IncidenciaDTO) -> Unit) {
    val context = LocalContext.current

    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("PENDIENTE") }
    var tipo by remember { mutableStateOf("ACADÉMICA") }
    var cifAlumno by remember { mutableStateOf("") }
    var cifProfesor by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf("") }

    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val calendar = Calendar.getInstance()

    fun showDatePicker() {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                calendar.set(year, month, dayOfMonth)
                fecha = dateFormatter.format(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            isError = titulo.isBlank()
        )

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            isError = descripcion.isBlank()
        )

        OutlinedTextField(
            value = fecha,
            onValueChange = {},
            label = { Text("Fecha") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = { showDatePicker() }) {
            Text("Seleccionar Fecha")
        }

        OutlinedTextField(value = estado, onValueChange = { estado = it }, label = { Text("Estado") })
        OutlinedTextField(value = tipo, onValueChange = { tipo = it }, label = { Text("Tipo") })

        OutlinedTextField(
            value = cifAlumno,
            onValueChange = { cifAlumno = it },
            label = { Text("CIF Alumno") },
            isError = cifAlumno.isBlank()
        )

        OutlinedTextField(
            value = cifProfesor,
            onValueChange = { cifProfesor = it },
            label = { Text("CIF Profesor") },
            isError = cifProfesor.isBlank()
        )

        if (errorMessage.isNotBlank()) {
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Button(onClick = {
            if (titulo.isBlank() || descripcion.isBlank() || fecha.isBlank()
                || cifAlumno.isBlank() || cifProfesor.isBlank()
            ) {
                errorMessage = "Por favor, completa todos los campos obligatorios."
                return@Button
            }

            errorMessage = ""

            val dto = IncidenciaDTO(
                id = null,
                titulo = titulo,
                descripcion = descripcion,
                fecha = fecha,
                estado = estado,
                tipo = tipo,
                cifAlumno = cifAlumno,
                cifProfesor = cifProfesor,
                nombreAlumno = null,
                nombreProfesor = null
            )
            onSubmit(dto)
        }) {
            Text("Registrar")
        }
    }
}