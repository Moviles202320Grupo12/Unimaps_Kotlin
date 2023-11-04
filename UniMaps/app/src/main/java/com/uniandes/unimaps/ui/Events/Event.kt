package com.uniandes.unimaps.ui.Events

import com.google.firebase.Timestamp

data class Event(
    private var id: String, // An identifier for the event, you can choose a suitable type
    val name: String,
    val description: String,
    val date: Timestamp?, // You can use a suitable date representation, e.g., String or Date
    val location: String,
    val url: String,
    val urlImage:String
) {
    constructor(): this("", "", "", null, "", "", "")

    // Setter para el campo "id" en Firestore
    fun setId(id: String) {
        this.id = id
    }

    fun getId(): String {
        return this.id
    }
}

