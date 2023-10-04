package com.uniandes.unimaps

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
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
        val events = listOf(
            Event("1","Event 1", "Crazy? I was crazy once. They locked me in a room. A rubber room! A rubber room with rats,and rats make me crazy. Crazy? I was crazy once. They locked me in a room. A rubber room! A rubber room with rats,and rats make me crazy. Crazy? I was crazy once. They locked me in a room. A rubber room! A rubber room with rats,and rats make me crazy. Crazy? I was crazy once. They locked me in a room. A rubber room! A rubber room with rats,and rats make me crazy. Crazy? I was crazy once. They locked me in a room. A rubber room! A rubber room with rats,and rats make me crazy. 1","09/09/2023","google.com",".com"),
            Event("2","Event 1", "Description 1","09/09/2023","google.com","google.com"),
            Event("3","Event 1", "Description 1","09/09/2023","google.com","google.com"),
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

}