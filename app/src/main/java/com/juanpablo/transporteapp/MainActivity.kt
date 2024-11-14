package com.juanpablo.transporteapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.activity.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private val mapViewModel: MapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()

        if (mapViewModel.map == null) {
            createFragment()
        } else {
            onMapReady(mapViewModel.map!!)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.map)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun createFragment() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mapViewModel.map = googleMap

        if (!mapViewModel.initialCameraPositionSet) {
            val coordinate = LatLng(19.721886, -101.184871)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 15f))
            mapViewModel.initialCameraPositionSet = true
        }

        createMarker(googleMap)
    }

    private fun createMarker(map: GoogleMap) {
        val coordinate = LatLng(19.721886, -101.184871)
        val marker = MarkerOptions().position(coordinate).title("Tec de Morelia")
        map.addMarker(marker)
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinate, 18f),
            500,
            null
        )
    }
}


