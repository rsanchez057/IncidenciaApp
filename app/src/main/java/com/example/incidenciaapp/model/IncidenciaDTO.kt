package com.example.incidenciaapp.model

data class IncidenciaDTO(
    val id: Int?,
    val titulo: String,
    val descripcion: String,
    val fecha: String,
    val estado: String,
    val tipo: String,
    val cifAlumno: String?,
    val cifProfesor: String?,
    val nombreAlumno: String?,
    val nombreProfesor: String?
)