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

        // Cambia el color de la barra de estado a un color definido en los recursos
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue_background)

        // Obtiene el TextView donde se mostrará el nombre de la aplicación y aplica estilos HTML
        val textView = findViewById<TextView>(R.id.app_name)
        textView.text = Html.fromHtml(getString(R.string.app_name_colored), Html.FROM_HTML_MODE_LEGACY)

        // Inicializa el fragmento del mapa en segundo plano para precargarlo
        // Esto puede ayudar a reducir el tiempo de carga del mapa en actividades posteriores
        val mapFragment = SupportMapFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .add(mapFragment, "mapFragment") // Agrega el fragmento del mapa al fragment manager
            .commit()
        mapFragment.getMapAsync(this) // Solicita notificación cuando el mapa esté listo

        // Usa corutinas para retrasar la transición al LoginActivity
        GlobalScope.launch {
            delay(6000) // Espera 6 segundos (6000 milisegundos)
            // Cambia a LoginActivity después del retraso
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            finish() // Finaliza el SplashActivity para que no esté en la pila de actividades
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // Este callback se ejecuta cuando el mapa está listo
        // No necesitas realizar ninguna acción aquí en el SplashActivity
    }
}
