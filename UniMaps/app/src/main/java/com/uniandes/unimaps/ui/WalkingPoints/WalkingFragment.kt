package com.uniandes.unimaps.ui.WalkingPoints

import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.uniandes.unimaps.R
import com.uniandes.unimaps.databinding.FragmentWpBinding


class WalkingFragment  : Fragment() {
    private lateinit var sensorManager: SensorManager
    private lateinit var stepSensor: Sensor
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var textViewSteps: TextView
    private lateinit var textCal : TextView
    private lateinit var textKm : TextView
    private lateinit var textMin : TextView
    private lateinit var textBonos : TextView
    private var currentSteps: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_wp, container, false)
        textViewSteps = rootView.findViewById(R.id.textViewPuntos)
        textKm=rootView.findViewById(R.id.textViewKm)
        textMin=rootView.findViewById(R.id.textViewMin)
        textBonos=rootView.findViewById(R.id.textViewBonos)
        textCal=rootView.findViewById(R.id.textViewCal)

        // Inicializa el sensorManager y el sensor
        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)?: run {
            // Manejar el caso en que el sensor es nulo
            // Puedes mostrar un mensaje de error o deshabilitar la funcionalidad de contar pasos
            // Por ejemplo, puedes mostrar un mensaje de error en el TextView
            textViewSteps.text = "Sensor de contador de pasos no disponible en este dispositivo"
            return rootView // O regresar una vista vacía o lo que sea apropiado para tu caso
        }

        // Inicializa las preferencias compartidas
        sharedPreferences = requireContext().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)

        // Recupera el valor almacenado de pasos
        currentSteps = sharedPreferences.getInt("stepCount", 0)
        textViewSteps.text = currentSteps.toString()
        textCal.text = ((currentSteps.toFloat()*0.05).toInt()).toString()
        textKm.text = ((currentSteps.toFloat()*0.0005).toInt()).toString()
        textBonos.text = ((currentSteps.toFloat()*0.00001).toInt()).toString()
        textMin.text = ((currentSteps.toFloat()*0.005).toInt()).toString()

        return rootView
    }

    override fun onResume() {
        super.onResume()

        // Registra el SensorEventListener cuando el fragmento se reanuda
        if (stepSensor != null) {
            sensorManager.registerListener(stepListener, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()

        // Detiene la escucha del SensorEventListener cuando el fragmento se pausa
        sensorManager.unregisterListener(stepListener)
    }

    private val stepListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            // Obtiene el número de pasos desde el evento
            val steps = event.values[0].toInt()

            // Calcula los nuevos pasos totales
            currentSteps += steps

            // Actualiza el TextView con el nuevo número de pasos
            textViewSteps.text = currentSteps.toString()
            textCal.text = ((currentSteps.toFloat()*0.05).toInt()).toString()
            textKm.text = ((currentSteps.toFloat()*0.0005).toInt()).toString()
            textBonos.text = ((currentSteps.toFloat()*0.00001).toInt()).toString()
            textMin.text = ((currentSteps.toFloat()*0.005).toInt()).toString()

            // Almacena el nuevo número de pasos en las preferencias compartidas
            val editor = sharedPreferences.edit()
            editor.putInt("stepCount", currentSteps)
            editor.apply()
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Maneja cambios en la precisión si es necesario.
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Libera la vinculación cuando la vista se destruye
    }
}

