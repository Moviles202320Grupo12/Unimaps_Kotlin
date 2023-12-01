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
import androidx.core.widget.addTextChangedListener
import com.uniandes.unimaps.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider
import com.uniandes.unimaps.helpers.Network
import kotlinx.coroutines.withContext
import okio.BufferedSink
import okio.BufferedSource
import okio.IOException
import okio.buffer
import okio.sink
import okio.source
import java.io.File
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class EventsFeedActivity : AppCompatActivity()  {
    private lateinit var listViewEvents: ListView
    private lateinit var editTextSearch: EditText
    private lateinit var buttonClear: Button
    private lateinit var eventViewModel: EventViewModel
    private lateinit var events: MutableList<Event> // Declare it as a member variable
    private lateinit var filteredEvents: List<Event>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.eventosfeed)

        // Inicializar el ViewModel
        eventViewModel = ViewModelProvider(this).get(EventViewModel::class.java)

        //actionbar
        val myToolbar: Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(myToolbar)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FED353")))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //set actionbar title
        myToolbar.title = "Eventos"
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FED353")))

        events = mutableListOf() // Initialize it in onCreate
        // Initialize UI components
        listViewEvents = findViewById(R.id.listViewEvents)
        editTextSearch = findViewById(R.id.editTextSearch)
        buttonClear = findViewById(R.id.FilterButton1)

        // Create an ArrayAdapter to populate the ListView with event names
        val adapter = EventAdapter(this, events)
        // Set the adapter for the ListView
        listViewEvents.adapter = adapter

        // Handle item clicks
        val original=events



        listViewEvents.setOnItemClickListener { _, _, position, _ ->
            val selectedEvent = events[position] // Get the selected event
            val intent = Intent(this, EventDetailActivity::class.java)
            intent.putExtra("selectedEvent", selectedEvent)
            startActivity(intent)
        }




        // Handle search
        editTextSearch.addTextChangedListener { text ->
            val searchText = text.toString()

            // Filter events based on search text
             filteredEvents = events.filter { event ->
                         event.name.contains(searchText, ignoreCase = true) ||
                         event.description.contains(searchText, ignoreCase = true) ||
                         event.location.contains(searchText, ignoreCase = true)


            }

            events= filteredEvents.toMutableList()
            val adapter = EventAdapter(this, events)

            listViewEvents.adapter=adapter

        }


        // Handle filter button click
        buttonClear.setOnClickListener {
            events= original
            val adapter = EventAdapter(this, events)

            listViewEvents.adapter=adapter



            Toast.makeText(this, "reset Done", Toast.LENGTH_SHORT).show()
        }

    }



    override fun onResume() {
        super.onResume()
        // Inicializar el contexto de CoroutineScope:
        val coroutineScope = CoroutineScope(Dispatchers.Main)
        val internetCheck = Network.checkConnectivity(this@EventsFeedActivity)
        if (internetCheck) {

            coroutineScope.launch {
                try {
                    val dbEvents = eventViewModel.getDBEvents();
                    Log.d("TAG", "La BQ fue llamada exitosamente")

                    val eventsList = dbEvents.values.toList()
                    saveEventsToCache(eventsList)
                    events.clear()
                    events.addAll(eventsList)
                    // Crear un ArrayAdapter en el contexto principal
                    withContext(Dispatchers.Main) {
                        val adapter = EventAdapter(this@EventsFeedActivity, events)
                        listViewEvents.adapter = adapter
                    }

                } catch (exception: Exception) {
                    Log.e("TAG", "Error en la carga del BD de eventos: ${exception.message}")
                }

            }
        }
        else{
            val cachelista=loadEventsFromCache()
            if (!cachelista.isNullOrEmpty()){
                coroutineScope.launch {
                    try {
                        val dbEvents = eventViewModel.getDBEvents();
                        Log.d("TAG", "La BQ fue llamada exitosamente")

                        events.clear()
                        events.addAll(cachelista)
                        // Crear un ArrayAdapter en el contexto principal
                        withContext(Dispatchers.Main) {
                            val adapter = EventAdapter(this@EventsFeedActivity, events)
                            listViewEvents.adapter = adapter
                        }

                    } catch (exception: Exception) {
                        Log.e("TAG", "Error en la carga del BD de eventos: ${exception.message}")
                    }

                }

            }else{
                Toast.makeText(this, "No hay internet, cuando haya se cargaran los eventos", Toast.LENGTH_SHORT).show()

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

    private fun saveEventsToCache(events: List<Event>) {
        try {
            // Abre un archivo para escribir en caché (puedes usar el contexto para obtener el directorio de caché)
            val cacheFile = File(cacheDir, "events_cache.txt")
            val sink: BufferedSink = cacheFile.sink().buffer()

            // Convierte la lista de eventos a un formato de texto (por ejemplo, JSON)
            val eventsJson = convertEventsToJson(events)

            // Escribe los datos en el archivo de caché
            sink.writeString(eventsJson, Charsets.UTF_8)
            sink.flush()
            sink.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun loadEventsFromCache(): List<Event>? {
        try {
            // Abre el archivo de caché para leer
            val cacheFile = File(cacheDir, "events_cache.txt")
            if (cacheFile.exists()) {
                val source: BufferedSource = cacheFile.source().buffer()
                val cacheData = source.readString(Charsets.UTF_8)
                source.close()

                // Convierte el texto en una lista de eventos (por ejemplo, desde JSON)
                return convertJsonToEvents(cacheData)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    // Función para convertir una lista de eventos a JSON
    private fun convertEventsToJson(events: List<Event>): String {
        val gson = Gson()
        return gson.toJson(events)
    }

    // Función para convertir JSON a una lista de eventos
    private fun convertJsonToEvents(json: String): List<Event> {
        val gson = Gson()
        val eventType = object : TypeToken<List<Event>>() {}.type
        return gson.fromJson(json, eventType)
    }
}