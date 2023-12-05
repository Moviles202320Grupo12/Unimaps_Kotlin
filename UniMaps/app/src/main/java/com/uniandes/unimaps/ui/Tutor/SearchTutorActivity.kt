package com.uniandes.unimaps.ui.Tutor

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
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uniandes.unimaps.R.*
import com.uniandes.unimaps.helpers.Network
import com.uniandes.unimaps.ui.Events.Event
import com.uniandes.unimaps.ui.Events.EventAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.BufferedSink
import okio.BufferedSource
import okio.IOException
import okio.buffer
import okio.sink
import okio.source
import java.io.File


class TutorsSearchActivity : AppCompatActivity() {
    private lateinit var filteredTutor: List<Tutor>
    private lateinit var listViewTutor: ListView
    private lateinit var editTextSearch: EditText
    private lateinit var buttonFilter: Button
    private lateinit var tutorViewModel: TutorViewModel
    private lateinit var tutores: MutableList<Tutor> // Declare it as a member variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.tutors_search)

        tutorViewModel= ViewModelProvider(this).get(TutorViewModel::class.java)

        // Initialize UI components and implement search functionality
        listViewTutor = findViewById(id.listViewTutores)
        editTextSearch = findViewById(id.editTextSearch2)
        buttonFilter = findViewById(id.FilterButton2)

        //actionbar
        val myToolbar: Toolbar = findViewById(id.my_toolbar)
        setSupportActionBar(myToolbar)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FED353")))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //set actionbar title
        supportActionBar!!.title = "Tutores"
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FED353")))
        tutores= mutableListOf()
       val original= tutores
        val adapter=TutorAdapter(this,tutores)

        listViewTutor.adapter=adapter

        listViewTutor.setOnItemClickListener { _, _, position, _ ->
            val selectedTutor = tutores[position] // Get the selected event
            val intent = Intent(this, TutorInfoActivity::class.java)
            intent.putExtra("selectedTutor", selectedTutor)
            startActivity(intent)
        }


        buttonFilter.setOnClickListener{

            tutores=original
            val adapter = TutorAdapter(this, tutores)
            listViewTutor.adapter=adapter

        }


        editTextSearch.addTextChangedListener { text ->
            val searchText = text.toString()

            // Filter events based on search text
            filteredTutor= tutores.filter { tutor ->
                tutor.name.contains(searchText, ignoreCase = true) ||
                tutor.description.contains(searchText, ignoreCase = true) ||
                        tutor.location.contains(searchText, ignoreCase = true) ||
                        tutor.materia.contains(searchText, ignoreCase = true)


            }

            tutores= filteredTutor.toMutableList()
            for (tutor in tutores){
                tutor.aumentarBusquedas()
            }
            val adapter = TutorAdapter(this, tutores)

            listViewTutor.adapter=adapter
        }
    }

    override fun onResume() {
        super.onResume()
        // Inicializar el contexto de CoroutineScope:
        val coroutineScope = CoroutineScope(Dispatchers.Main)
        val internetCheck = Network.checkConnectivity(this@TutorsSearchActivity)
        if (internetCheck) {

            coroutineScope.launch {
                try {
                    val dbEvents = tutorViewModel.getDBTutors();
                    Log.d("TAG", "La BQ fue llamada exitosamente")

                    val tutoresList = dbEvents.values.toList()
                    tutores.clear()
                    tutores.addAll(tutoresList)
                    saveTutoresToCache(tutoresList)
                    // Create an ArrayAdapter to populate the ListView with event names
                    // Crear un ArrayAdapter en el contexto principal
                    withContext(Dispatchers.Main) {
                        val adapter = TutorAdapter(this@TutorsSearchActivity, tutores)
                        listViewTutor.adapter = adapter
                    }

                } catch (exception: Exception) {
                    Log.e("TAG", "Error en la carga del BD de eventos: ${exception.message}")
                }

            }
        }else{
            val cachelista=loadTutoresFromCache()
            if (!cachelista.isNullOrEmpty()){
                coroutineScope.launch {
                    try {
                        val dbTutor = tutorViewModel.getDBTutors();
                        Log.d("TAG", "La BQ fue llamada exitosamente")

                        tutores.clear()
                        tutores.addAll(cachelista)
                        // Crear un ArrayAdapter en el contexto principal
                        withContext(Dispatchers.Main) {
                            val adapter = TutorAdapter(this@TutorsSearchActivity, tutores)
                            listViewTutor.adapter = adapter
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

    private fun saveTutoresToCache(tutores: List<Tutor>) {
        try {
            // Abre un archivo para escribir en caché (puedes usar el contexto para obtener el directorio de caché)
            val cacheFile = File(cacheDir, "tutores_cache.txt")
            val sink: BufferedSink = cacheFile.sink().buffer()

            // Convierte la lista de eventos a un formato de texto (por ejemplo, JSON)
            val tutoresJson = convertTutoresToJson(tutores)

            // Escribe los datos en el archivo de caché
            sink.writeString(tutoresJson, Charsets.UTF_8)
            sink.flush()
            sink.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun loadTutoresFromCache(): List<Tutor>? {
        try {
            // Abre el archivo de caché para leer
            val cacheFile = File(cacheDir, "tutores.txt")
            if (cacheFile.exists()) {
                val source: BufferedSource = cacheFile.source().buffer()
                val cacheData = source.readString(Charsets.UTF_8)
                source.close()

                // Convierte el texto en una lista de eventos (por ejemplo, desde JSON)
                return convertJsonToTutores(cacheData)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun convertTutoresToJson(tutores: List<Tutor>): String {
        val gson = Gson()
        return gson.toJson(tutores)
    }

    // Función para convertir JSON a una lista de eventos
    private fun convertJsonToTutores(json: String): List<Tutor> {
        val gson = Gson()
        val tutorType = object : TypeToken<List<Tutor>>() {}.type
        return gson.fromJson(json, tutorType)
    }
}