package com.uniandes.unimaps.ui.Events

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.uniandes.unimaps.R
import java.util.Date
import com.google.firebase.firestore.FirebaseFirestore

class EventDetailActivity : AppCompatActivity() {

    private val firestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.event_detail)

        //actionbar
        val myToolbar: Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(myToolbar)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FED353")))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //set actionbar title

        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FED353")))


        val selectedEvent = intent.getParcelableExtra<Event>("selectedEvent")

        if (selectedEvent != null) {
            // Now, it  can use the selectedEvent to fill your textviews or perform any other actions.
            val eventNameTextView = findViewById<TextView>(R.id.eventDetailName)
            val eventDescriptionTextView = findViewById<TextView>(R.id.eventDetailDescription)
            val eventDateTextView = findViewById<TextView>(R.id.eventDetailDate)
            val eventLocationTextView = findViewById<TextView>(R.id.eventDetailLocation)
            val eventPopularityTextView =findViewById<TextView>(R.id.eventDetailPopularity)

            eventNameTextView.text = "Detalle de " + selectedEvent.name
            supportActionBar!!.title= eventNameTextView.text
            eventDescriptionTextView.text = selectedEvent.description
            eventPopularityTextView.text = selectedEvent.popularity.toString()
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
                // Increment the popularity
                selectedEvent.incrementPopularity()

                // Update the popularity in the database
                updatePopularityInDatabase(selectedEvent.id, selectedEvent.popularity)

                openWebPage(selectedEvent.url)

            }
        }



    }
    // Method to open a web page in a web browser
    private fun openWebPage(url: String) {
        val webPageUri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webPageUri)
            startActivity(intent)
        
    }
    // Function to update popularity in the database
    private fun updatePopularityInDatabase(eventId: String, newPopularity: Int) {
        // Assuming you have a "events" collection in your Firestore
        val eventRef = firestore.collection("events").document(eventId)

        // Update the "popularity" field with the new value
        eventRef.update("popularity", newPopularity)
            .addOnSuccessListener {
                Log.d("TAG", "Popularity updated in the database")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error updating popularity in the database", e)
                // Handle the error, e.g., show a toast message
                Toast.makeText(this, "Error updating popularity.", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Realiza la acción que desees cuando se presiona el botón de "Atrás"
                onBackPressed() // Esto es común para volver a la actividad anterior
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}