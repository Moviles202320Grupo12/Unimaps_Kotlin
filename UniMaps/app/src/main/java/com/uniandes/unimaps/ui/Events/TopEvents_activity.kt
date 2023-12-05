package com.uniandes.unimaps.ui.Events

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.uniandes.unimaps.R
import com.uniandes.unimaps.helpers.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TopEvents_activity : AppCompatActivity() {
    private lateinit var listViewEvents: ListView
    private lateinit var eventViewModel: EventViewModel
    private lateinit var events: MutableList<Event> // Declare it as a member variable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popular_events)

        // Inicializar el ViewModel
        eventViewModel = ViewModelProvider(this).get(EventViewModel::class.java)

        //actionbar
        val myToolbar: Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(myToolbar)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FED353")))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //set actionbar title

        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FED353")))
        supportActionBar!!.title = "Eventos populares"
        events = mutableListOf() // Initialize it in onCreate
        // Initialize UI components
        listViewEvents = findViewById(R.id.listViewTopEvents)


        // Create an ArrayAdapter to populate the ListView with event names
        val adapter = TopEventAdapter(this, events)
        // Set the adapter for the ListView
        listViewEvents.adapter = adapter

        // Handle item clicks
        val original = events



        listViewEvents.setOnItemClickListener { _, _, position, _ ->
            val selectedEvent = events[position] // Get the selected event
            val intent = Intent(this, EventDetailActivity::class.java)
            intent.putExtra("selectedEvent", selectedEvent)
            startActivity(intent)
        }


    }



    override fun onResume() {
        super.onResume()
        // Inicializar el contexto de CoroutineScope:
        val coroutineScope = CoroutineScope(Dispatchers.Main)
        val internetCheck = Network.checkConnectivity(this@TopEvents_activity)
        if (internetCheck) {

            coroutineScope.launch {
                try {
                    val dbEvents = eventViewModel.getDBEvents();
                    Log.d("TAG", "La BQ fue llamada exitosamente")

                    val eventsList = dbEvents.values.toList()
                    val topEventsList = eventsList.sortedByDescending { it.popularity }
                    events.clear()
                    events.addAll(topEventsList)
                    // Crear un ArrayAdapter en el contexto principal
                    withContext(Dispatchers.Main) {
                        val adapter = TopEventAdapter(this@TopEvents_activity, events)
                        listViewEvents.adapter = adapter
                    }

                } catch (exception: Exception) {
                    Log.e("TAG", "Error en la carga del BD de eventos: ${exception.message}")
                }

            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Realiza la acción que desees cuando se presiona el botón de "Atrás"
                onBackPressed() // Esto es común para volver a la actividad anterior
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}