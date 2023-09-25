package com.uniandes.unimaps

import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.search.SearchBar

class EventsFeedActivity : AppCompatActivity()  {
    private lateinit var filterButton : Button
    private lateinit var  searchBar: SearchBar
    private lateinit var input: TextView
    private  lateinit var  lista: ListView
    private val eventos= listOf<String>("Evento a","evento b","evento c");
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.eventosfeed)

        filterButton= findViewById(R.id.filterButton)
        searchBar= findViewById(R.id.EventSearch)

    }
}