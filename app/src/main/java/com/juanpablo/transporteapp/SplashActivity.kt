package com.juanpablo.transporteapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue_background)

        setContentView(R.layout.activity_splash)
        // Obtener el TextView y aplicar el texto con colores diferentes
        val textView = findViewById<TextView>(R.id.app_name)
        textView.text = Html.fromHtml(getString(R.string.app_name_colored), Html.FROM_HTML_MODE_LEGACY)

        // Iniciar la función de carga con un delay de 3 segundos
        GlobalScope.launch {
            delay(3000) // Delay de 3 segundos
            // Redirige a MainActivity
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            finish() // Finaliza la SplashActivity para que no se regrese a ella
        }
    }

}