package com.example.incidenciaapp.screen

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.incidenciaapp.model.IncidenciaDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterIncidenceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegisterIncidenceForm { dto ->
                val token = getToken(this)
                if (token != null) {
                    registrarIncidencia(token, dto)
                } else {
                    Toast.makeText(this, "No autenticado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        return sharedPref.getString("jwt_token", null)
    }

    private fun registrarIncidencia(token: String, dto: IncidenciaDTO) {
        RetrofitInstance.authApi.registrarIncidencia("Bearer $token", dto)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@RegisterIncidenceActivity, "Incidencia registrada", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@RegisterIncidenceActivity, "Error al registrar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@RegisterIncidenceActivity, "Error de red", Toast.LENGTH_SHORT).show()
                }
            })
    }
}