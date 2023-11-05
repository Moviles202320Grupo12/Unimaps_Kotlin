package com.uniandes.unimaps.ui.WalkingPoints

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.uniandes.unimaps.R
import com.uniandes.unimaps.databinding.FragmentWpBinding
import com.uniandes.unimaps.helpers.Network
import com.uniandes.unimaps.ui.Login.LogInViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class WalkingFragment  : AppCompatActivity(){
    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var textViewSteps: TextView
    private lateinit var textCal : TextView
    private lateinit var textKm : TextView
    private lateinit var textMin : TextView
    private lateinit var textBonos : TextView
    private var currentSteps: Int = 0
    private var newSteps: Int = 0

    private lateinit var binding: FragmentWpBinding

    private lateinit var wpViewModel: WalkingViewModel

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        // Inicializar el ViewModel
        wpViewModel = ViewModelProvider(this).get(WalkingViewModel::class.java)

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Walking Points"
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FED353")))
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
        binding = FragmentWpBinding.inflate(layoutInflater)
        val root: View = binding.root
        // Inicializa el sensorManager y el sensor
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            // Muestra un mensaje al usuario
            val toast = Toast.makeText(this, "Sensor de contador de pasos no disponible en este dispositivo", Toast.LENGTH_LONG)
            toast.show()
        }else{
            val toast = Toast.makeText(this, "Activo!", Toast.LENGTH_LONG)
            toast.show()
        }
        // Inicializa las preferencias compartidas
        sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)

        // Recupera el valor almacenado de pasos
        // Recupera los datos almacenados en SharedPreferences
        currentSteps = sharedPreferences.getInt("stepCount", 0)



        textViewSteps = root.findViewById(R.id.textViewPuntos)
        textKm=root.findViewById(R.id.textViewKm)
        textMin=root.findViewById(R.id.textViewMin)
        textBonos=root.findViewById(R.id.textViewBonos)
        textCal=root.findViewById(R.id.textViewCal)
        loadCachedData()


        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()

        // Registra el SensorEventListener cuando el fragmento se reanuda
        if (stepSensor != null) {
            sensorManager.registerListener(stepListener, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }
    private fun loadCachedData() {
        // Recupera el valor almacenado de pasos
        currentSteps = sharedPreferences.getInt("stepCount", 0)
        textViewSteps.text = currentSteps.toString()
        textCal.text = ((currentSteps.toFloat() * 0.05).toInt()).toString()
        textKm.text = ((currentSteps.toFloat() * 0.0005).toInt()).toString()
        textBonos.text = ((currentSteps.toFloat() * 0.00001).toInt()).toString()
        textMin.text = ((currentSteps.toFloat() * 0.005).toInt()).toString()
    }

    override fun onPause() {
        super.onPause()

        // Almacena el nuevo número de pasos en las preferencias compartidas
        // Detiene la escucha del SensorEventListener cuando el fragmento se pausa
        sensorManager.unregisterListener(stepListener)

        val editor = sharedPreferences.edit()
        editor.putInt("stepCount", currentSteps+newSteps)
        editor.apply()

        val net=Network.checkConnectivity(this)
        if (net){
            // Inicializar el contexto de CoroutineScope:
            val coroutineScope = CoroutineScope(Dispatchers.Main)
            coroutineScope.launch {
                try {
                    wpViewModel.updateWalkingPoints(currentSteps+newSteps)
                    Log.d("TAG", "Walking Points Actualizados!")
                }
                catch  (exception: Exception)
                {
                    Log.e("TAG", "Error en la actualizacion de los walking points: ${exception.message}")
                }
            }

        }else{
            val toast = Toast.makeText(this, "Ups! No cuentas con coneccion en este momento, conectate para subir tus puntos", Toast.LENGTH_LONG)
            toast.show()
        }
    }

    private val stepListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            // Obtiene el número de pasos desde el evento
            val steps = event.values[0].toInt()

            // Calcula los nuevos pasos totales
            newSteps=steps

            // Actualiza el TextView con el nuevo número de pasos
            textViewSteps.text = (currentSteps+steps).toString()
            textCal.text = ((currentSteps.toFloat()*0.05).toInt()).toString()
            textKm.text = ((currentSteps.toFloat()*0.0005).toInt()).toString()
            textBonos.text = ((currentSteps.toFloat()*0.00001).toInt()).toString()
            textMin.text = ((currentSteps.toFloat()*0.005).toInt()).toString()

        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Maneja cambios en la precisión si es necesario.
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

