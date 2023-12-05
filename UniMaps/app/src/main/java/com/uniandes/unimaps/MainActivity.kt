package com.uniandes.unimaps

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.uniandes.unimaps.databinding.ActivityMainBinding


class MainActivity: AppCompatActivity() {


    // Preferencias:
    var prefs: SharedPreferences? = null
    var valor:Boolean? = null

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    // Objeto de autenticacion con auth:
    companion object {
        lateinit var auth: FirebaseAuth
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        valor = intent.extras?.getBoolean("valorRecibido");

        if(valor!=true){
            auth = Firebase.auth

            if(auth.currentUser == null)
            {
                startActivity(Intent(this, LogInActivity::class.java))
                finish()
            }
            else
            {
                saveUserInfo();
            }
        }
        // Autenticación con Firebase:
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Se obtiene el NavController
        navController = findNavController(R.id.nav_host_fragment_content_main)

        setSupportActionBar(binding.appBarMain.toolbar)


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_Feed, R.id.nav_log_out
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Set navigation listener to toolbar
        findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = ""
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FED353")))
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setHomeAsUpIndicator(R.drawable.baseline_menu_24)

    }

    private fun saveUserInfo() {
        // Guardar preferencias para cada vez que se inicie sesion:
        prefs = getSharedPreferences("com.uniandes.unimaps", MODE_PRIVATE);
        prefs!!.edit().putString("userUid", auth.currentUser?.uid).apply()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Llama a la pantalla de inicio (menú principal de Android)
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // Método para cerrar sesión
    fun logout() {
        auth.signOut()

        val intent = Intent(this, LogInActivity::class.java)
        intent.putExtra("key", "value")
        startActivity(intent)

        finish()

    }

}