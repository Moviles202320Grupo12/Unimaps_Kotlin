package com.uniandes.unimaps

data class Tutor(
    val id: Int, // An identifier for the event, you can choose a suitable type
    val name: String,
    val description: String,
    val materia: String,
    val date: String, // You can use a suitable date representation, e.g., String or Date
    val location: String,
    val imageUrl : String
)

