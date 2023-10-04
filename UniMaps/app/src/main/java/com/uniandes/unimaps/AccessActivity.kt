package com.uniandes.unimaps

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.uniandes.unimaps.databinding.AccessBinding
import com.uniandes.unimaps.viewmodels.LogInViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AccessActivity : AppCompatActivity() {
    // Esto hace el binding con la vista xml de Registro:
    private lateinit var binding: AccessBinding

    private lateinit var loginViewModel: LogInViewModel

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

        // Inicializar el ViewModel
        loginViewModel = ViewModelProvider(this).get(LogInViewModel::class.java)

        binding = AccessBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginbutton.setOnClickListener(View.OnClickListener {
            // Se inicia sesion:
            // Manda a ventana principal:
            val username = binding.email.text.toString()
            val password = binding.password.text.toString()
            if(username != "" && password!= "")
            {
                // Inicializar el contexto de CoroutineScope
                val coroutineScope = CoroutineScope(Dispatchers.Main)

                // Llamar a la función verifyUserLogIn en un contexto de Coroutine
                coroutineScope.launch {
                    try {
                        val puedeIngresar = loginViewModel.verifyUserLogIn(username, password)
                        if(puedeIngresar.isNotEmpty())
                        {
                            val intent = Intent(this@AccessActivity, MainActivity::class.java)
                            startActivity(intent)
                        }
                        else
                        {
                            val toast = Toast.makeText(applicationContext, "Usuario Errado!", Toast.LENGTH_LONG)
                            toast.show()
                        }

                    }
                    catch  (e: Exception)
                    {
                        val toast = Toast.makeText(applicationContext, "Error al conectar con la BD", Toast.LENGTH_LONG)
                        toast.show()
                    }
                }
                }

            else{
                val toast = Toast.makeText(applicationContext, "Por favor rellene todos los campos", Toast.LENGTH_LONG)
                toast.show()
            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}