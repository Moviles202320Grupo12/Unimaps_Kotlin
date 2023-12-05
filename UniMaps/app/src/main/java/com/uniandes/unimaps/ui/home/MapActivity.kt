package com.uniandes.unimaps.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.uniandes.unimaps.MainActivity
import com.uniandes.unimaps.R
import com.uniandes.unimaps.databinding.ActivityMapBinding

class MapActivity: AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cargar el fragmento fragment_home en el FrameLayout
        val fragmentHome = HomeFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragmentHome)
            .commit()

    }
}