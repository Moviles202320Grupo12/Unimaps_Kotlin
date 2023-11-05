package com.uniandes.unimaps.ui.Events

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.uniandes.unimaps.R
import java.util.Date

class EventDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.event_detail)
        val selectedEvent = intent.getParcelableExtra<Event>("selectedEvent")

        if (selectedEvent != null) {
            // Now, it  can use the selectedEvent to fill your textviews or perform any other actions.
            val eventNameTextView = findViewById<TextView>(R.id.eventDetailName)
            val eventDescriptionTextView = findViewById<TextView>(R.id.eventDetailDescription)
            val eventDateTextView = findViewById<TextView>(R.id.eventDetailDate)
            val eventLocationTextView = findViewById<TextView>(R.id.eventDetailLocation)

            eventNameTextView.text = selectedEvent.name
            eventDescriptionTextView.text = selectedEvent.description
            // Check if the date is not null before formatting
            val date = selectedEvent?.date
            if (date != null) {
                // Convert the Timestamp to a Date object
                val dateObj = Date(date.seconds * 1000) // Convert seconds to milliseconds

                // Format the date and time in "day/month/year hour:minute" format
                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
                val formattedDate = dateFormat.format(dateObj)
                eventDateTextView.text = formattedDate
            } else {
                // Handle the case when the date is null, e.g., set text to "Date not available"
                eventDateTextView.text = "Date not available"
            }

            eventLocationTextView.text = selectedEvent.location
            val eventDetailImage = findViewById<ImageView>(R.id.eventDetailImage)

            // Load the event's image using Glide
            Glide.with(this)
                .load(selectedEvent.urlImage)
                .placeholder(R.drawable.feria) // You can use a placeholder image
                .error(R.drawable.retiros) // You can use an error image
                .into(eventDetailImage)
        } else {

            Log.d("TAG","The parsing of the selected event failed")

        }

        val inscribirButton = findViewById<Button>(R.id.eventDetailInscriptionButton)
        inscribirButton.setOnClickListener {
            // Call the openWebPage method when the "Inscribir" button is clicked
            if (selectedEvent != null) {
                openWebPage(selectedEvent.url)
            }
        }



    }
    // Method to open a web page in a web browser
    private fun openWebPage(url: String) {
        val webPageUri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webPageUri)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            // Handle the case where there is no app to handle the URL
            // You can show a toast message or display an error to the user
            Toast.makeText(this, "No app can handle this request.", Toast.LENGTH_SHORT).show()
        }
    }
}