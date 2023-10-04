package com.uniandes.unimaps

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener

class EventsFeedActivity : AppCompatActivity()  {
    private lateinit var listViewEvents: ListView
    private lateinit var editTextSearch: EditText
    private lateinit var buttonFilter: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.eventosfeed)

        // Initialize UI components
        listViewEvents = findViewById(R.id.listViewEvents)
        editTextSearch = findViewById(R.id.editTextSearch)
        buttonFilter = findViewById(R.id.FilterButton1)

        // Sample list of events (replace this with your actual event data)
        val events = mutableListOf(
            Event(1,"Event 1", "Description 1","09/09/2023","google.com","google.com"),
            Event(2,"Event 1", "Description 1","09/09/2023","google.com","google.com"),
            Event(3,"Event 1", "Description 1","09/09/2023","google.com","google.com"),
        )

        // Create an ArrayAdapter to populate the ListView with event names
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, events.map { it.id })

        // Set the adapter for the ListView
        listViewEvents.adapter = adapter

        // Handle item clicks
        listViewEvents.setOnItemClickListener { _, _, position, _ ->
            val clickedEvent = events[position]
            // Handle the click event here
            // For example, you can open a new activity to display event details
            Toast.makeText(this, "Clicked on ${clickedEvent.name}", Toast.LENGTH_SHORT).show()
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

}