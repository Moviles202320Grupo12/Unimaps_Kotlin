import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.uniandes.unimaps.R
import com.uniandes.unimaps.ui.Events.Event
import java.util.Date

class EventDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        val selectedEventId = intent.getStringExtra("selected_event_id")

        // Initialize Firebase Firestore
        val db = FirebaseFirestore.getInstance()

        // Reference to the events collection in Firestore (Update with your actual Firestore collection name)
        val eventsRef = db.collection("events")

        // Fetch the event details using the selectedEventId
        if (selectedEventId != null) {
            eventsRef.document(selectedEventId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val event = document.toObject(Event::class.java)
                        if (event != null) {
                            // Now you have the event details in the 'event' object
                            // You can populate your UI elements with this data
                            val eventNameTextView = findViewById<TextView>(R.id.eventNameTextView)
                            val eventDescriptionTextView =
                                findViewById<TextView>(R.id.eventDescriptionTextView)
                            val eventLocationTextView =
                                findViewById<TextView>(R.id.eventLocationTextView)
                            val eventDateTextView = findViewById<TextView>(R.id.eventDateTextView)

                            eventNameTextView.text = event.name
                            eventDescriptionTextView.text = event.description
                            eventLocationTextView.text = event.location
                            eventDateTextView.text = event.date?.toDate().toString()

                            // Check if the date is not null before formatting
                            val date = event?.date
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
                        }
                    } else {
                        // Handle the case where the document with the given ID doesn't exist
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle the error, e.g., display an error message
                }
        }
    }
}
