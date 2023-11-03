package com.uniandes.unimaps.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.uniandes.unimaps.R
import com.uniandes.unimaps.databinding.FragmentHomeBinding
import com.uniandes.unimaps.helpers.Network


class HomeFragment : Fragment(), OnMapReadyCallback {

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

        // Configurar Image View:
        val myImageView = root.findViewById<ImageView>(R.id.imageView6)

        myImageView.setOnClickListener {
            val url = "https://uniandes.edu.co"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        if (Network.checkConnectivity(requireContext()))
        {
            // Referencia al FrameLayout donde esta el mapa:
            val layoutContenedorMapa = view.findViewById<FrameLayout>(R.id.contenedorMapa)

            // Cambia la visibilidad del FrameLayout:
            layoutContenedorMapa.visibility = View.VISIBLE


            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
            mapFragment?.getMapAsync(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val current = LatLng(4.60142035, -74.0649170096208)
        mMap.addMarker(MarkerOptions()
            .position(current)
            .title("Uniandes"))
        val cameraPosition = CameraPosition.builder()
            .target(LatLng(4.60142035, -74.0649170096208))
            .zoom(16f)
            .bearing(0f)
            .tilt(45f)
            .build()
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
}