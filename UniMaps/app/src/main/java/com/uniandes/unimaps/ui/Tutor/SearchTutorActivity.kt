package com.uniandes.unimaps.ui.Tutor

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.uniandes.unimaps.R
import com.uniandes.unimaps.R.*
import com.uniandes.unimaps.ui.Events.EventDetailActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TutorsSearchActivity : AppCompatActivity() {
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
        myToolbar.title = "Tutores"
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FED353")))
        tutores= mutableListOf()

        val adapter=TutorAdapter(this,tutores)

        listViewTutor.adapter=adapter

        listViewTutor.setOnItemClickListener { _, _, position, _ ->
            val selectedTutor = tutores[position] // Get the selected event
            val intent = Intent(this, EventDetailActivity::class.java)
            intent.putExtra("selectedTutor", selectedTutor)
            startActivity(intent)
        }





        editTextSearch.addTextChangedListener { text ->
            val searchText= text.toString()
            adapter.filter.filter(searchText)
        }
    }

    override fun onResume() {
        super.onResume()
        // Inicializar el contexto de CoroutineScope:
        val coroutineScope = CoroutineScope(Dispatchers.Main)

        coroutineScope.launch {
            try {
                val dbEvents = tutorViewModel.getDBTutors();
                Log.d("TAG", "La BQ fue llamada exitosamente")

                val tutoresList=dbEvents.values.toList()
                tutores.clear()
                tutores.addAll(tutoresList)
                // Create an ArrayAdapter to populate the ListView with event names
                // Crear un ArrayAdapter en el contexto principal
                withContext(Dispatchers.Main) {
                    val adapter = TutorAdapter(this@TutorsSearchActivity, tutores)
                    listViewTutor.adapter = adapter
                }

            }
            catch  (exception: Exception)
            {
                Log.e("TAG", "Error en la carga del BD de eventos: ${exception.message}")
            }

        }
    }
}