


package com.juanpablo.transporteapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Dot
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    // Usamos un ViewModel para mantener el estado del mapa y evitar reinicializarlo innecesariamente
    private val mapViewModel: MapViewModel by viewModels()

    lateinit var btnMuestraBottonSheet : Button


    companion object {
        const val REQUEST_CODE_LOCATION = 0 // Código único para la solicitud de permisos de ubicación
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge() // Activa el modo Edge-to-Edge para ajustar la UI a toda la pantalla

        // Si el mapa aún no se ha inicializado, crea el fragmento del mapa
        if (mapViewModel.map == null) {
            createFragment()
        } else {
            onMapReady(mapViewModel.map!!)
        }
        val vista = layoutInflater.inflate(R.layout.bottom_sheet_rutas, null)



        val btnMuestraBottomSheet: Button = findViewById(R.id.btnRutasCercanas)
        btnMuestraBottomSheet.setOnClickListener {
            val rutas = listOf(
                Ruta("Gris 1", (0..6).random()),
                Ruta("Coral", (4..11).random()),
                Ruta("Oro-verde", (9..20).random())
            ).sortedBy { it.tiempo } // Ordenar por tiempo ascendente
            mostrarBottomSheet(rutas)
        }

        // Ajusta el padding del mapa para adaptarse a las barras del sistema (estatus y navegación)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.map)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun mostrarBottomSheet(rutas: List<Ruta>) {
        val vista = layoutInflater.inflate(R.layout.bottom_sheet_rutas, null)
        val container = vista.findViewById<LinearLayout>(R.id.rutasContainer)

        // Limpiar vistas previas en el contenedor (por si se llama varias veces)
        container.removeAllViews()

        rutas.forEach { ruta ->
            val itemView = layoutInflater.inflate(R.layout.item_ruta, container, false)

            // Configurar datos de cada ruta
            val nombreTextView = itemView.findViewById<TextView>(R.id.tvRutaNombre)
            val tiempoTextView = itemView.findViewById<TextView>(R.id.tvRutaTiempo)

            nombreTextView.text = ruta.nombre
            tiempoTextView.text = "A ${ruta.tiempo} min"

            // Cambiar el color según el tiempo
            val color = when (ruta.tiempo) {
                in 0..5 -> Color.GREEN
                in 6..10 -> Color.YELLOW
                else -> Color.RED
            }
            tiempoTextView.setTextColor(color)

            // Agregar la vista al contenedor
            container.addView(itemView)
        }

        // Mostrar el Bottom Sheet
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(vista)
        bottomSheetDialog.show()
    }

    // Inicializa el fragmento del mapa y solicita la notificación cuando esté listo
    private fun createFragment() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    // Método llamado cuando el mapa está listo para usarse
    override fun onMapReady(googleMap: GoogleMap) {
        mapViewModel.map = googleMap // Guarda la instancia del mapa en el ViewModel
        enableLocation() // Intenta habilitar la ubicación en el mapa
        createPolyLines()

        // Establece la posición inicial del mapa si aún no se ha configurado
        if (!mapViewModel.initialCameraPositionSet) {
            googleMap.setOnMyLocationChangeListener { location ->
                // Verifica si la posición inicial ya fue configurada
                if (!mapViewModel.initialCameraPositionSet) {
                    // Obtén la ubicación actual del usuario
                    val userLocation = LatLng(location.latitude, location.longitude)

                    // Mueve la cámara a la ubicación actual del usuario
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 18f))

                    // Marca que la posición inicial ya fue configurada
                    mapViewModel.initialCameraPositionSet = true

                    // Elimina el listener para evitar que se ejecute de nuevo
                    googleMap.setOnMyLocationChangeListener(null)
                }
            }
        }


//        createMarker(googleMap) // Crea un marcador en las coordenadas especificadas
    }
    private fun createPolyLines(){
        val polylineOptions = PolylineOptions()
            .add(LatLng(19.68542061510658, -101.2355877049008))
            .add(LatLng(19.68474028140797, -101.23699329551224))
            .add(LatLng(19.68164545380128, -101.23568565947694))
            .add(LatLng(19.678948738596986, -101.23447504195268))
            .add(LatLng(19.678551785162668, -101.23414866105131))
            .add(LatLng(19.678231660707993, -101.23361829208697))
            .add(LatLng(19.67730969870607, -101.23118403453199))
            .add(LatLng(19.676829515105368, -101.22979336194669))
            .add(LatLng(19.67666570864442, -101.22517748243138))
            .add(LatLng(19.676627302639844, -101.22285206985224))
            .add(LatLng(19.677056273558378, -101.22054020513522))
            .add(LatLng(19.677024260843027, -101.22023422304012))
            .add(LatLng(19.67631997948493, -101.21863631654458))
            .add(LatLng(19.673214710170726, -101.21232631152762))
            .add(LatLng(19.672043114862873, -101.20983092350514))
            .add(LatLng(19.671921462704844, -101.20936855056166))
            .add(LatLng(19.67199829565753, -101.20864779273809))
            .add(LatLng(19.672151961453153, -101.20822621740756))
            .add(LatLng(19.673336463680513, -101.20622033478521))
            .add(LatLng(19.67514863001061, -101.20315618736109))
            .add(LatLng(19.67554324525328, -101.20278821186596))
            .add(LatLng(19.67597635842526, -101.20251223024493))
            .add(LatLng(19.67842101971017, -101.20172517154658))
            .add(LatLng(19.679940756948312, -101.20129522485706))
            .add(LatLng(19.680357646679013, -101.20111358297636))
            .add(LatLng(19.680731777565057, -101.20065947827376))
            .add(LatLng(19.68091349796586, -101.20012590524848))
            .add(LatLng(19.680999013376976, -101.1995015112827))
            .add(LatLng(19.68105246048637, -101.19891117516947))
            .add(LatLng(19.68108452874307, -101.19858194926029))
            .add(LatLng(19.680934876822917, -101.19776456079593))
            .add(LatLng(19.68088142967494, -101.19727639824076))
            .add(LatLng(19.68090105521803, -101.19703295537026))
            .add(LatLng(19.681146660991686, -101.19654853134446))
            .add(LatLng(19.68130455021982, -101.19613863409215))
            .add(LatLng(19.68179576015433, -101.19426614891583))
            .add(LatLng(19.682313279707913, -101.19080995728635))
            .add(LatLng(19.682304508142437, -101.19028826987407))
            .add(LatLng(19.682120305178984, -101.18958026552832))
            .add(LatLng(19.681120342535024, -101.18753077926571))
            .add(LatLng(19.680979996277316, -101.18703703885164))
            .add(LatLng(19.680944909724758, -101.18645014051306))
            .add(LatLng(19.681532616737883, -101.1774201679482))
            .add(LatLng(19.68202110231387, -101.16984955373381))
            .add(LatLng(19.68217021910729, -101.16914154938863))
            .add(LatLng(19.682494766951052, -101.16827517560817))
            .add(LatLng(19.684064867467256, -101.16585305547972))
            .add(LatLng(19.686678735924517, -101.16221057375422))
            .add(LatLng(19.68784531791502, -101.16042193119745))
            .add(LatLng(19.68787163169641, -101.16003066563839))
            .add(LatLng(19.687719013423163, -101.15875571981434))
            .add(LatLng(19.687494156851912, -101.15712380370789))
            .add(LatLng(19.687359242758163, -101.1557386651587))
            .add(LatLng(19.68722319607234, -101.15464677088531))
            .add(LatLng(19.687155711290515, -101.1536719983911))
            .add(LatLng(19.68738515943363, -101.15294091902045))
            .add(LatLng(19.68805325656635, -101.15227434665331))
            .add(LatLng(19.68864036992676, -101.15172962084793))
            .add(LatLng(19.68914345219683, -101.15123410075296))
            .add(LatLng(19.68974004417339, -101.1507507059127))
            .add(LatLng(19.690453491454647, -101.15043715250265))
            .add(LatLng(19.69120998811539, -101.15017585799447))
            .add(LatLng(19.692119329670575, -101.14984270973203))
            .add(LatLng(19.69305417708462, -101.1495552857727))
            .add(LatLng(19.69391558720025, -101.14928707489496))
            .add(LatLng(19.69491192636194, -101.149104168739))
            .add(LatLng(19.695594599614708, -101.14908457165095))
            .add(LatLng(19.696234219668284, -101.14924788274269))
            .add(LatLng(19.696732382807994, -101.14971168049489))
            .add(LatLng(19.69727359532577, -101.1505086287448))
            .add(LatLng(19.69818407746044, -101.15178281447852))
            .add(LatLng(19.69896513785649, -101.15279533069824))
            .add(LatLng(19.69932799138867, -101.15324606372506))
            .add(LatLng(19.699807694795993, -101.15387970290746))
            .add(LatLng(19.700415480234213, -101.15465051888464))
            .add(LatLng(19.700968980276315, -101.15532335224314))
            .add(LatLng(19.70157782811181, -101.15608763868002))
            .add(LatLng(19.702088274511397, -101.15675393967624))
            .add(LatLng(19.702567969643695, -101.1573614494079))
            .add(LatLng(19.70318910896694, -101.15814532954172))
            .add(LatLng(19.70375489918092, -101.1588704218021))
            .add(LatLng(19.70429608794751, -101.15955631988638))
            .add(LatLng(19.7047573269287, -101.16017036198073))
            .add(LatLng(19.705341562780262, -101.16090198970869))
            .add(LatLng(19.706067240101703, -101.16178385867391))
            .add(LatLng(19.706743715996836, -101.162626533463))
            .add(LatLng(19.70727874490514, -101.16324710792044))
            .add(LatLng(19.70812739780918, -101.16398525347334))
            .add(LatLng(19.708785415923955, -101.16437719523562))
            .add(LatLng(19.70926509097444, -101.16472341045909))
            .add(LatLng(19.709744764587043, -101.165102287496))
            .add(LatLng(19.7100214987086, -101.16531132310269))
            .add(LatLng(19.710255184927362, -101.1652655965639))
            .add(LatLng(19.71050117005184, -101.16544850271947))
            .add(LatLng(19.710667209797265, -101.1659906888243))
            .add(LatLng(19.71139534553184, -101.16694926885248))
            .add(LatLng(19.712157892749616, -101.16793565562104))
            .add(LatLng(19.712723651239514, -101.16865421551873))
            .add(LatLng(19.71360638251707, -101.16984264514682))
            .add(LatLng(19.714270527566967, -101.17076370828852))
            .add(LatLng(19.71477478400375, -101.17143000928434))
            .add(LatLng(19.715162199453232, -101.1719525983011))
            .add(LatLng(19.715734091595507, -101.17271687861128))
            .add(LatLng(19.716269090426394, -101.17342890614628))
            .add(LatLng(19.71668724918605, -101.17399722170182))
            .add(LatLng(19.717037763774485, -101.17447408417942))
            .add(LatLng(19.717566608891318, -101.17512078808718))
            .add(LatLng(19.71794786823959, -101.17567603891736))
            .add(LatLng(19.718464233328206, -101.17636859745936))
            .add(LatLng(19.71898077513285, -101.17707409263168))
            .add(LatLng(19.719435822481444, -101.17766200528254))
            .add(LatLng(19.71977403250969, -101.17812580303475))
            .add(LatLng(19.720149137341025, -101.17867452150223))
            .add(LatLng(19.72029671932863, -101.17916444870517))
            .add(LatLng(19.72030901782138, -101.17984381442676))
            .add(LatLng(19.720105886330785, -101.18043238917873))
            .add(LatLng(19.71975537847031, -101.18105949599845))
            .add(LatLng(19.71939872055951, -101.18176499117078))
            .add(LatLng(19.719220391305655, -101.18248355106849))
            .add(LatLng(19.719294182745656, -101.18334582294602))
            .add(LatLng(19.719552452502555, -101.18403172114895))
            .add(LatLng(19.719872211061272, -101.18488743547546))
            .add(LatLng(19.720376449828862, -101.18594567823396))
            .add(LatLng(19.72083764243098, -101.1868994031891))
            .add(LatLng(19.72134802735924, -101.18808829320169))
            .add(LatLng(19.721889156588233, -101.18927718321427))
            .add(LatLng(19.722658165031874, -101.19089810955724))
            .add(LatLng(19.723168544147384, -101.19194328759035))
            .add(LatLng(19.72359898309793, -101.1928970125455))
            .add(LatLng(19.724005235050626, -101.19383868872268))
            .add(LatLng(19.724386479041087, -101.19483160785431))
            .add(LatLng(19.72467548597578, -101.19592251242622))
            .add(LatLng(19.724816914711212, -101.19684357556794))
            .add(LatLng(19.724939890637643, -101.19784954995511))
            .add(LatLng(19.725136660692172, -101.19925400793667))
            .add(LatLng(19.725321132397596, -101.20075645135933))
            .add(LatLng(19.725474858724482, -101.20208252111816))
            .add(LatLng(19.72564087747243, -101.2033497595085))
            .add(LatLng(19.72578230535339, -101.20455824660912))
            .add(LatLng(19.725972925342532, -101.2059169780523))
            .add(LatLng(19.726095905859523, -101.20690989718355))
            .add(LatLng(19.72621815307913, -101.20808664162509))
            .add(LatLng(19.726390325512938, -101.20930166108879))
            .add(LatLng(19.726519454727622, -101.2103207097066))
            .add(LatLng(19.726648583827043, -101.21136588773973))
            .add(LatLng(19.72683136220293, -101.2129656201806))
            .add(LatLng(19.726985087008572, -101.21413491310513))
            .add(LatLng(19.727132662683545, -101.21526501185355))
            .add(LatLng(19.72729253617753, -101.2165910814829))
            .add(LatLng(19.727483132614907, -101.21825681958984))
            .add(LatLng(19.72763070782986, -101.21939998306362))
            .add(LatLng(19.727735240191535, -101.22036024038145))
            .add(LatLng(19.72775983603124, -101.2212290446217))
            .add(LatLng(19.727587665073145, -101.22204558996005))
            .add(LatLng(19.72730885844898, -101.22262780898866))
            .add(LatLng(19.726866131504153, -101.22318305981844))
            .add(LatLng(19.72632501912821, -101.22363379284526))
            .add(LatLng(19.725826948160986, -101.22397347570566))
            .add(LatLng(19.72509281414763, -101.22447866656802))
            .add(LatLng(19.723955233609644, -101.22526255009296))
            .add(LatLng(19.723125102160225, -101.22581780092274))
            .add(LatLng(19.72215354111212, -101.22653634509774))
            .add(LatLng(19.72132340048745, -101.22717651648881))
            .add(LatLng(19.72064698627129, -101.2276729760548))
            .add(LatLng(19.71985373322515, -101.22811717671857))
            .add(LatLng(19.719054326948125, -101.22865936282338))
            .add(LatLng(19.717864636108615, -101.22943661564007))
            .add(LatLng(19.716948381794396, -101.22998533410758))
            .add(LatLng(19.71597586247762, -101.23066525331612))
            .add(LatLng(19.71510264346935, -101.23125969832242))
            .add(LatLng(19.714284765002347, -101.23186720805407))
            .add(LatLng(19.71325232298257, -101.2326118815402))
            .add(LatLng(19.712391388133, -101.23322592379431))
            .add(LatLng(19.711579645380255, -101.23378770698717))
            .add(LatLng(19.71086857633658, -101.23431623407605))
            .add(LatLng(19.709878486181708, -101.23501519688568))
            .add(LatLng(19.709072880352466, -101.2355181888144))
            .add(LatLng(19.708316468132693, -101.23601464838002))
            .add(LatLng(19.707689199889387, -101.23660255390347))
            .add(LatLng(19.70717877136036, -101.23734724325192))
            .add(LatLng(19.706717539360724, -101.2384054860104))
            .add(LatLng(19.706379301634954, -101.23978381471228))
            .add(LatLng(19.70605951264514, -101.24108841120449))
            .add(LatLng(19.705670680317724, -101.24251360336105))
            .add(LatLng(19.70549848557063, -101.24319296908226))
            .add(LatLng(19.70505569822548, -101.24408137041053))
            .add(LatLng(19.704489912611805, -101.24457782997618))
            .add(LatLng(19.703874925979804, -101.24480646267092))
            .add(LatLng(19.70318613814564, -101.24482605975899))
            .add(LatLng(19.702400790679278, -101.24454893250343))
            .add(LatLng(19.701441397973326, -101.24412432892733))
            .add(LatLng(19.700771049656296, -101.24387609914453))
            .add(LatLng(19.700217548929587, -101.24387609914453))
            .add(LatLng(19.69976859693361, -101.24358214282253))
            .add(LatLng(19.6992765932998, -101.24323592759906))
            .add(LatLng(19.69834179840724, -101.24280565015401))
            .add(LatLng(19.69768373733791, -101.24255088800808))
            .add(LatLng(19.69698262256793, -101.24223080223575))
            .add(LatLng(19.69635530674897, -101.24194991063911))
            .add(LatLng(19.69574406506827, -101.2417082111472))
            .add(LatLng(19.694926087625362, -101.2413685282868))
            .add(LatLng(19.693917448029964, -101.2409504570734))
            .add(LatLng(19.69293531281818, -101.2405231991261))
            .add(LatLng(19.69178772310576, -101.24000807931859))
            .add(LatLng(19.690629238494466, -101.23953938657206))
            .add(LatLng(19.6897275485685, -101.23914035010148))
            .add(LatLng(19.689092263979873, -101.23886465217637))
            .add(LatLng(19.688641415323374, -101.2386615063366))
            .add(LatLng(19.688197396467245, -101.23848012612285))
            .add(LatLng(19.687657738225937, -101.23823345177223))
            .add(LatLng(19.68713174329136, -101.23802305072437))
            .add(LatLng(19.686441799263875, -101.23774009759074))
            .add(LatLng(19.685717696394676, -101.23742086841402))
            .add(LatLng(19.685116551903135, -101.23718870174011))
            .add(LatLng(19.68476816031206, -101.2370363423605))
            .add(LatLng(19.685034577478888, -101.2363543527563))
            .add(LatLng(19.68536247492257, -101.2357449152374))

            .color(ContextCompat.getColor(this, R.color.ruta1))

        val polyline = mapViewModel.map?.addPolyline(polylineOptions)

        val points = polyline?.points // Obtén los puntos de la polilínea
        if (points != null && points.size >= 2) {
            val lineColor = polyline.color // Obtiene el color de la línea
            for (i in 0 until points.size - 1) {
                val start = points[i]
                val end = points[i + 1]

                // Calcula el ángulo entre los dos puntos
                val angle = Math.toDegrees(
                    Math.atan2(
                        (end.longitude - start.longitude),
                        (end.latitude - start.latitude)
                    )
                )

                // Genera el marcador con el color de la línea
                val arrowMarker = MarkerOptions()
                    .position(start)
                    .icon(getColoredArrowBitmap(this, lineColor)) // Usa la flecha con el color de la línea
                    .anchor(0.5f, 0.5f) // Centro del icono
                    .rotation(angle.toFloat()) // Rotación de la flecha
                    .flat(true) // Para que siga la orientación del mapa

                mapViewModel.map?.addMarker(arrowMarker)
            }
        }

    }
    // Agrega un marcador en una ubicación específica
    private fun createMarker(map: GoogleMap) {
        val coordinate = LatLng(19.721886, -101.184871) // Coordenadas del Tec de Morelia
        val marker = MarkerOptions().position(coordinate).title("Tec de Morelia")
        map.addMarker(marker)
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinate, 18f),
            500, // Duración de la animación en milisegundos
            null // Callback opcional para animación
        )
    }

    // Verifica si los permisos de ubicación han sido otorgados
    private fun isLocationPermissionsGranted() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PERMISSION_GRANTED

    // Habilita la funcionalidad de ubicación en el mapa si los permisos están concedidos
    private fun enableLocation() {
        if (mapViewModel.map == null) return // Si el mapa no está inicializado, no hacer nada
        if (isLocationPermissionsGranted()) {
            try {
                mapViewModel.map?.isMyLocationEnabled = true // Activa la ubicación en el mapa
            } catch (e: SecurityException) {
                e.printStackTrace()
                println("Excepción de seguridad al habilitar la ubicación: ${e.message}")
            }
        } else {
            requestLocationPermission() // Solicita permisos si no están otorgados
        }
    }

    // Solicita permisos de ubicación al usuario
    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Explica al usuario por qué se necesitan los permisos
            Toast.makeText(this, "Ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
        } else {
            // Solicita los permisos de ubicación
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION
            )
        }
    }

    // Maneja la respuesta del usuario a la solicitud de permisos
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        when (requestCode) {
            REQUEST_CODE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                    try {
                        mapViewModel.map?.isMyLocationEnabled = true // Activa la ubicación si se concede el permiso
                    } catch (e: SecurityException) {
                        e.printStackTrace()
                        println("Excepción de seguridad al habilitar la ubicación: ${e.message}")
                    }
                } else {
                    // Muestra un mensaje si el usuario deniega el permiso
                    Toast.makeText(this, "Para activar la localizacion ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {}
        }
    }

    // Verifica el estado de los permisos cuando la actividad vuelve a estar activa
    override fun onResumeFragments() {
        super.onResumeFragments()
        if (mapViewModel.map == null) return // Si el mapa no está inicializado, no hacer nada
        if (!isLocationPermissionsGranted()) {
            try {
                mapViewModel.map?.isMyLocationEnabled = false // Desactiva la ubicación si no hay permisos
            } catch (e: SecurityException) {
                e.printStackTrace()
                println("Excepción de seguridad al habilitar la ubicación: ${e.message}")
            }
            Toast.makeText(this, "Para activar la localizacion ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
        }
    }
    fun getColoredArrowBitmap(context: Context, color: Int, scale: Float = 0.03f): BitmapDescriptor {
        val arrowDrawable = ContextCompat.getDrawable(context, R.drawable.arrow)!! // Tu flecha en drawable
        arrowDrawable.setTint(color) // Aplica el color

        // Tamaño original del drawable
        val width = (arrowDrawable.intrinsicWidth * scale).toInt() // Escala el ancho
        val height = (arrowDrawable.intrinsicHeight * scale).toInt() // Escala el alto

        // Crear un bitmap escalado
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        arrowDrawable.setBounds(0, 0, canvas.width, canvas.height)
        arrowDrawable.draw(canvas)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

}
