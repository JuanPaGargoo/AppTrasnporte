package com.juanpablo.transporteapp

import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Referencia al botón de iniciar sesión
        val btnIniciarSesion = findViewById<Button>(R.id.btnIniciarSesion)

        // Configura el listener para el botón
        btnIniciarSesion.setOnClickListener {
            // Crea el Intent para ir a MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            // Opcional: Finaliza LoginActivity si no deseas que el usuario pueda volver a esta pantalla
            finish()
        }
    }
}
