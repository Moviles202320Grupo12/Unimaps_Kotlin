package com.uniandes.unimaps

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import com.uniandes.unimaps.databinding.LogInBinding


class LogInActivity : ComponentActivity() {
    // Esto hace el binding con la vista xml de Log In:
    private lateinit var binding: LogInBinding

    // Se pueden necesitar mas adelante:
    lateinit var username : EditText
    lateinit var password: EditText
    lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.registerButton.setOnClickListener(View.OnClickListener {
            // Manda a ventana de ingreso:
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        })
        binding.loginButton.setOnClickListener(View.OnClickListener {
            // Manda a ventana de ingreso:
            val intent = Intent(this, AccessActivity::class.java)
            startActivity(intent)
        })
    }
}
