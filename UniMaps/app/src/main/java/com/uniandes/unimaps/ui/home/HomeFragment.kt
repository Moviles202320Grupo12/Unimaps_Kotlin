package com.uniandes.unimaps.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.uniandes.unimaps.MainActivity
import com.uniandes.unimaps.R
import com.uniandes.unimaps.databinding.FragmentHomeBinding
import com.uniandes.unimaps.helpers.Network
import com.uniandes.unimaps.ui.Events.EventsFeedActivity
import com.uniandes.unimaps.ui.WalkingPoints.WalkingFragment


class HomeFragment : Fragment(), OnMapReadyCallback {

    private val LATITUD_UNIANDES = 4.60142035

    private val LONGITUD_UNIANDES = -74.0649170096208

    private var _binding: FragmentHomeBinding? = null

    private lateinit var mMap: GoogleMap

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Button Home:
        val buttonHome = root.findViewById<ImageView>(R.id.home_button)
        buttonHome.setOnClickListener {
            val intent = Intent (getActivity(), MainActivity::class.java)
            getActivity()?.startActivity(intent)
        }

        // Button Home:
        val buttonWalkingPoints = root.findViewById<ImageView>(R.id.wp_button)
        buttonWalkingPoints.setOnClickListener {
            val intent = Intent (getActivity(), WalkingFragment::class.java)
            getActivity()?.startActivity(intent)
        }

        // Button Home:
        val buttonSearch = root.findViewById<ImageView>(R.id.search_button)
        buttonSearch.setOnClickListener {
            val intent = Intent (getActivity(), EventsFeedActivity::class.java)
            getActivity()?.startActivity(intent)
        }

        // Boton de error en caso de ser necesario:
        val goBackButton =root.findViewById<Button>(R.id.back_button)
        goBackButton.setOnClickListener {
            val intent = Intent (getActivity(), MainActivity::class.java)
            getActivity()?.startActivity(intent)

        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        if (Network.checkConnectivity(requireContext()))
        {
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
            mapFragment?.getMapAsync(this)
        }
        else
        {
            // Referencia al FrameLayout donde esta el error:
            val layoutContenedorError = view.findViewById<RelativeLayout>(R.id.contenedorError)

            // Cambia la visibilidad del FrameLayout:
            layoutContenedorError.visibility = View.VISIBLE

            // Referencia al FrameLayout donde esta el mapa:
            val layoutContenedorMapa = view.findViewById<LinearLayout>(R.id.contenedorMapa)

            // Cambia la visibilidad del FrameLayout:
            layoutContenedorMapa.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val current = LatLng(LATITUD_UNIANDES, LONGITUD_UNIANDES)
        mMap.addMarker(MarkerOptions()
            .position(current)
            .title("Uniandes"))
        val cameraPosition = CameraPosition.builder()
            .target(LatLng(LATITUD_UNIANDES, LONGITUD_UNIANDES))
            .zoom(16f)
            .bearing(0f)
            .tilt(45f)
            .build()
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        val alertadd = AlertDialog.Builder(requireContext())
        val factory = LayoutInflater.from(requireContext())
        val view: View = factory.inflate(R.layout.alert_dialog, null)

        // Button Home:
        val buttonSearch = view.findViewById<Button>(R.id.boton_uniandes)
        buttonSearch.setOnClickListener {
            // Define the URL you want to open
            val url = "https://uniandes.edu.co/"

            // Create an Intent with the ACTION_VIEW action and the URL
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

            // Start the activity
            startActivity(intent)
        }

        alertadd.setTitle("Bienvenido a la Universidad de Los Andes")

        alertadd.setPositiveButton("Cerrar"
        ) { dialog, _ -> dialog.cancel() }

        alertadd.setView(view)

        // Set custom dimensions for the AlertDialog (optional)
        val alertDialog = alertadd.create()

        alertDialog.show()

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
    }
}