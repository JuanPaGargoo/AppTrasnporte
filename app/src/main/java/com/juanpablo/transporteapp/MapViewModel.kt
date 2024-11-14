package com.juanpablo.transporteapp

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

class MapViewModel : ViewModel() {
    var map: GoogleMap? = null
    var initialCameraPositionSet = false
    val defaultLocation = LatLng(19.721886, -101.184871)  // Posición por defecto para el marcador
}
