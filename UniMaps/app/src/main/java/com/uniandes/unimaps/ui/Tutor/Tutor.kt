package com.uniandes.unimaps.ui.Tutor

import com.google.firebase.Timestamp

data class Tutor(
    private var id: String, // An identifier for the event, you can choose a suitable type
    val name: String,
    val description: String,
    val materia: String,
    val date: Timestamp?, // You can use a suitable date representation, e.g., String or Date
    val location: String,
    val imageUrl : String
) {

    constructor() : this("", "", "", "", null, "", "")

    fun setId(id: String) {
        this.id = id
    }

    fun getId() :String{
        return this.id
    }
}

