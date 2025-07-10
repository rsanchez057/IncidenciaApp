package com.example.incidenciaapp.screen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class CoordinatorMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoordinatorMainScreen(
                onVerIncidencias = {
                    startActivity(Intent(this, FacultyIncidencesActivity::class.java))
                },
                onRegistrarIncidencia = {
                    startActivity(Intent(this, RegisterIncidenceActivity::class.java))
                },
                onCerrarSesion = {
                    val prefs = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                    prefs.edit().remove("jwt_token").apply()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            )
        }
    }
}

@Composable
fun CoordinatorMainScreen(
    onVerIncidencias: () -> Unit,
    onRegistrarIncidencia: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Button(onClick = onVerIncidencias, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text("Ver incidencias de su facultad")
            }
            Button(onClick = onRegistrarIncidencia, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text("Registrar una nueva incidencia")
            }
            Button(onClick = onCerrarSesion, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text("Cerrar sesión")
            }
        }
    }
}