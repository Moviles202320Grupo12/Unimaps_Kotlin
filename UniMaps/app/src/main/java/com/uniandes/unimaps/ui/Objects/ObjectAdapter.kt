package com.uniandes.unimaps.ui.Events
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.uniandes.unimaps.R
import java.util.Date

class ObjectAdapter(
    context: Context,
    private val events: List<Event>
) : ArrayAdapter<Event>(context, 0, events) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView

        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(
                R.layout.event_list_item, parent, false
            )
        }

        val currentEvent = getItem(position)

        val eventNameTextView = listItemView!!.findViewById<TextView>(R.id.eventNameTextView)
        val eventDescriptionTextView = listItemView.findViewById<TextView>(R.id.eventDescriptionTextView)
        val eventDateTextView = listItemView.findViewById<TextView>(R.id.eventDateTextView)
        val eventLocationTextView = listItemView.findViewById<TextView>(R.id.eventLocationTextView)

        eventNameTextView.text = currentEvent?.name
        eventDescriptionTextView.text = currentEvent?.description

        // Check if the date is not null before formatting
        val date = currentEvent?.date
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

        eventLocationTextView.text = currentEvent?.location

        return listItemView
    }
}