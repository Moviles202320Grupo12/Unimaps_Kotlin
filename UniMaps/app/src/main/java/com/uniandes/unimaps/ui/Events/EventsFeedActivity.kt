package com.uniandes.unimaps.ui.Events

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.uniandes.unimaps.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider

class EventsFeedActivity : AppCompatActivity()  {
    private lateinit var listViewEvents: ListView
    private lateinit var editTextSearch: EditText
    private lateinit var buttonFilter: Button
    private lateinit var eventViewModel: EventViewModel
    private lateinit var events: MutableList<Event> // Declare it as a member variable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.eventosfeed)

        // Inicializar el ViewModel
        eventViewModel = ViewModelProvider(this).get(EventViewModel::class.java)

        events = mutableListOf() // Initialize it in onCreate
        // Initialize UI components
        listViewEvents = findViewById(R.id.listViewEvents)
        editTextSearch = findViewById(R.id.editTextSearch)
        buttonFilter = findViewById(R.id.FilterButton1)

        // Sample list of events (replace this with your actual event data)
         events = mutableListOf(
            Event("1","Event 1", "Description 1","09/09/2023","google.com",".com",""),
            Event("2","Event 1", "Description 2","09/09/2023","google.com","google.com",""),
            Event("3","Event 1", "Description 3","09/09/2023","google.com","google.com",""),
        )

        // Create an ArrayAdapter to populate the ListView with event names
        val adapter = EventAdapter(this, events)
        // Set the adapter for the ListView
        listViewEvents.adapter = adapter

        // Handle item clicks
        listViewEvents.setOnItemClickListener { _, _, position, _ ->
            val clickedEvent = events[position]
        // Assuming you're passing an event object
            val event = events[position]

            val intent = Intent(this, EventDetailsActivity::class.java)

                // Provide explicit type information to differentiate putExtra overloads
            intent.putExtra("event", event as Parcelable)

            startActivity(intent)


    }

        // Handle filter button click
        buttonFilter.setOnClickListener {
            // Implement filter logic here
            // You can update the 'events' list based on the filter criteria and then update the adapter
            Toast.makeText(this, "Filter button clicked", Toast.LENGTH_SHORT).show()
        }

        // Handle search
        editTextSearch.addTextChangedListener { text ->
            val searchText = text.toString()
            adapter.filter.filter(searchText)
        }
    }



    override fun onResume() {
        super.onResume()
        // Inicializar el contexto de CoroutineScope:
        val coroutineScope = CoroutineScope(Dispatchers.Main)

        coroutineScope.launch {
            try {
                val dbEvents = eventViewModel.getDBEvents();
                Log.d("TAG", "La BQ fue llamada exitosamente")

               val eventsList=dbEvents.values.toList()
                events.clear()
                events.addAll(eventsList)
                // Create an ArrayAdapter to populate the ListView with event names



            }
            catch  (exception: Exception)
            {
                Log.e("TAG", "Error en la carga del BD de eventos: ${exception.message}")
            }

        }
        val adapter = EventAdapter(this, events)
        // Set the adapter for the ListView
        listViewEvents.adapter = adapter
    }
}