package com.uniandes.unimaps.ui.Events
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.uniandes.unimaps.R
import java.net.HttpURLConnection
import java.net.URL
import java.text.ParseException
import java.util.Date

class TopEventAdapter(
    context: Context,
    private var events: List<Event> // Change to var to allow updating the dataset
) : ArrayAdapter<Event>(context, 0, events) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView

        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(
                R.layout.top_event_list_item, parent, false
            )
        }

        val currentEvent = getItem(position)

        val eventNameTextView = listItemView!!.findViewById<TextView>(R.id.eventNameTextView)
        val eventPopulary = listItemView!!.findViewById<TextView>(R.id.eventPopularityTextView)
        val eventimage = listItemView!!.findViewById<ImageView>(R.id.imageView2)
        eventNameTextView.text = currentEvent?.name
        eventPopulary.text = currentEvent?.popularity.toString()
        val imageLoader = EventImageLoader(eventimage)
        imageLoader.execute(currentEvent?.urlImage)



        return listItemView
    }

    private inner class EventImageLoader(private val imageView: ImageView) :
        AsyncTask<String, Void, Bitmap?>() {

        override fun doInBackground(vararg params: String?): Bitmap? {
            val imageUrl = params[0]
            return try {
                val url = URL(imageUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()

                val inputStream = connection.inputStream
                BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        override fun onPostExecute(result: Bitmap?) {
            if (result != null) {
                imageView.setImageBitmap(result)
            } else {
                // Set an error image if the download failed
                imageView.setImageResource(R.drawable.logo_sin_texto)
            }
        }

    }
}
