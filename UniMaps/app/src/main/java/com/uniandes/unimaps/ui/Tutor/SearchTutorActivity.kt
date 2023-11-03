package com.uniandes.unimaps.ui.Tutor

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.uniandes.unimaps.R.*


class TutorsSearchActivity : AppCompatActivity() {
    private lateinit var listViewTutor: ListView
    private lateinit var editTextSearch: EditText
    private lateinit var buttonFilter: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.tutors_search)

        // Initialize UI components and implement search functionality
        listViewTutor = findViewById(id.listViewTutores)
        editTextSearch = findViewById(id.editTextSearch2)
        buttonFilter = findViewById(id.FilterButton2)

        // Sample list of tutores (replace this with your actual tutor data)
        val tutores = mutableListOf(
            Tutor(1,"juan 1", "Description 1","ingles 1","09/09/2023","ML","google.com"),
            Tutor(2,"jose 1", "Description 1","Proba 1","09/09/2023","AU_123","google.com"),
            Tutor(3,"pablo 1", "Description 1","MEL 1","09/09/2023","PU_101","google.com"),
        )
        // Set up search functionality to filter tutors based on user input

        // Create an ArrayAdapter to populate the ListView with event names
        val adapter = TutorAdapter(this, tutores)
        // Set the adapter for the ListView
        listViewTutor.adapter = adapter

        // Handle item clicks
        listViewTutor.setOnItemClickListener { _, _, position, _ ->
            val clickedEvent = tutores[position]
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