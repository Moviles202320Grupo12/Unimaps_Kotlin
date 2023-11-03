package com.uniandes.unimaps.ui.Events
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.uniandes.unimaps.R

class EventAdapter(
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
        eventDateTextView.text = currentEvent?.date.toString()
        eventLocationTextView.text = currentEvent?.location

        return listItemView
    }
}