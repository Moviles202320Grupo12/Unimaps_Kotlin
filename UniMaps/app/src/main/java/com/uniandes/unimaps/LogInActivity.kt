package com.uniandes.unimaps

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.uniandes.unimaps.databinding.LogInBinding
import com.uniandes.unimaps.ui.Login.LogInViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LogInActivity : ComponentActivity() {

    // Preferencias:
    var prefs: SharedPreferences? = null

    // Esto hace el binding con la vista xml de Log In:
    private lateinit var binding: LogInBinding

    lateinit var loginButton: Button

    private lateinit var loginViewModel: LogInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar el ViewModel
        loginViewModel = ViewModelProvider(this).get(LogInViewModel::class.java)


        // Guardar preferencias:
        prefs = getSharedPreferences("com.uniandes.unimaps", MODE_PRIVATE);

        binding = LogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.registerButton.setOnClickListener(View.OnClickListener {
            // Manda a ventana de registro:
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        })
        binding.loginButton.setOnClickListener(View.OnClickListener {
            // Manda a ventana de acceso:
            val intent = Intent(this, AccessActivity::class.java)
            startActivity(intent)
        })
    }

    /**
     * Función que sobrescribe el metodo onResume. Si es la primera vez que se utiliza la aplicacion
     * se guarda firstrun con valor de true o false.
     */
    override fun onResume() {
        super.onResume()
        if (prefs!!.getBoolean("firstrun", true)) {
            // Se cambia el valor para que no vuelva a guardarse el valor.
            prefs!!.edit().putBoolean("firstrun", false).commit()

            // Se detecta el OS del usuario:
            val androidVersion = getAndroidVersion();

            if (androidVersion != null)
            {
                // Inicializar el contexto de CoroutineScope:
                val coroutineScope = CoroutineScope(Dispatchers.Main)

                coroutineScope.launch {
                    try {
                        loginViewModel.storeAndroidVersion(androidVersion);
                        Log.d("TAG", "La BQ fue guardada exitosamente")
                    }
                    catch  (exception: Exception)
                    {
                        Log.e("TAG", "Error en la consulta: ${exception.message}")
                    }
                }
            }
        }
    }

    /**
     * Función que obtiene la version del sistema operativo:
     */
    private fun getAndroidVersion(): String? {
        val release = Build.VERSION.RELEASE
        val sdkVersion = Build.VERSION.SDK_INT
        return "Android SDK: $sdkVersion, version: $release)"
    }
}
