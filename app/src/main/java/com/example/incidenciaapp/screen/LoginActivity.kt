package com.example.incidenciaapp.screen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.core.content.edit
import okhttp3.ResponseBody
import android.util.Base64
import org.json.JSONObject

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen()
        }
    }
}

@Composable
fun LoginScreen() {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                isLoading = true
                errorMessage = null
                loginUser(context, username, password) { success, message, rol ->
                    isLoading = false
                    if (success) {
                        val nextActivity = when (rol) {
                            "COORDINADOR" -> CoordinatorMainActivity::class.java
                            else -> MainActivity::class.java
                        }
                        context.startActivity(Intent(context, nextActivity))
                        if (context is ComponentActivity) context.finish()
                    } else {
                        errorMessage = message
                        Log.e("Login", "Error: $message")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text(if (isLoading) "Cargando..." else "Iniciar sesión")
        }
        errorMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
fun loginUser(
    context: Context,
    username: String,
    password: String,
    onResult: (Boolean, String, String?) -> Unit
) {
    val authApi = RetrofitInstance.getAuthApi(context)
    authApi.login(username, password).enqueue(object : Callback<ResponseBody> {
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            if (response.isSuccessful) {
                val token = response.body()?.string()
                if (!token.isNullOrBlank()) {
                    saveToken(context, token)
                    val rol = extractRoleFromToken(token)
                    if (rol != null) saveRole(context, rol)
                    onResult(true, "Login exitoso", rol)
                } else {
                    onResult(false, "Token vacío", null)
                }
            } else {
                onResult(false, "Error de login", null)
            }
        }

        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            onResult(false, "Error de red: ${t.localizedMessage}", null)
        }
    })
}

fun saveRole(context: Context, rol: String) {
    val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    sharedPref.edit {
        putString("user_role", rol)
    }
}

fun saveToken(context: Context, token: String) {
    val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    sharedPref.edit {
        putString("jwt_token", token)
    }
}


fun extractRoleFromToken(token: String): String? {
    val parts = token.split(".")
    if (parts.size < 2) return null
    val payload = String(Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP))
    val json = JSONObject(payload)
    val rolesArray = json.optJSONArray("roles") ?: return null
    return if (rolesArray.length() > 0) rolesArray.getString(0).removePrefix("ROLE_") else null
}