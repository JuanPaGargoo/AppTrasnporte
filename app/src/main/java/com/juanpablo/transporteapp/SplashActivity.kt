package com.juanpablo.transporteapp

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity(), OnMapReadyCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Cambia el color de la barra de estado
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue_background)

        // Configura el TextView con colores
        val textView = findViewById<TextView>(R.id.app_name)
        textView.text = Html.fromHtml(getString(R.string.app_name_colored), Html.FROM_HTML_MODE_LEGACY)

        // Inicializa el mapa para que comience a precargar en segundo plano
        val mapFragment = SupportMapFragment.newInstance()
        supportFragmentManager.beginTransaction().add(mapFragment, "mapFragment").commit()
        mapFragment.getMapAsync(this)

        // Inicia la transición a LoginActivity después de 3 segundos
        GlobalScope.launch {
            delay(6000)
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // No necesitas hacer nada aquí por ahora, solo dejar que el mapa se inicialice.
    }
}