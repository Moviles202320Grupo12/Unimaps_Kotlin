package com.uniandes.unimaps.ui.Events

import com.google.firebase.Timestamp

data class Event(
    val id: String, // An identifier for the event, you can choose a suitable type
    val name: String,
    val description: String,
    val date: Timestamp?, // You can use a suitable date representation, e.g., String or Date
    val location: String,
    val url: String,
    val urlImage:String
) {
    constructor(): this("", "", "", null, "", "", "")
}

