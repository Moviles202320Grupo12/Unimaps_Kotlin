package com.uniandes.unimaps

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.uniandes.unimaps.databinding.AccessBinding


class AccessActivity : AppCompatActivity() {
    // Esto hace el binding con la vista xml de Registro:
    private lateinit var binding: AccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = ""
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#000000")))
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        binding = AccessBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginbutton.setOnClickListener(View.OnClickListener {
            // Se inicia sesion:
            // TODO - Verificar si es necesario conectar con el back (mas adelante)
            // Manda a ventana principal:
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}