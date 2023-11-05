package com.uniandes.unimaps.ui.Tutor
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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

class TutorAdapter(
    context: Context,
    private val tutores: List<Tutor>
) : ArrayAdapter<Tutor>(context, 0, tutores) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView

        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(
                R.layout.tutor_list_item, parent, false
            )
        }

        val currentTutor = getItem(position)
        // Inside EventAdapter's getView method
        val tutorImageView = listItemView!!.findViewById<ImageView>(R.id.tutorImageView)


        val tutorNameTextView = listItemView!!.findViewById<TextView>(R.id.tutorNameTextView)
        val tutorMateriaTextView = listItemView.findViewById<TextView>(R.id.tutorMateriaTextView)
        val tutorDateTextView = listItemView.findViewById<TextView>(R.id.tutorDateTextView)
        val tutorLocationTextView = listItemView.findViewById<TextView>(R.id.tutorLocationTextView)
        val tutorDescriptionTextView =listItemView.findViewById<TextView>(R.id.TutorDescriptionTextView)

        tutorNameTextView.text = currentTutor?.name
        tutorMateriaTextView.text = currentTutor?.materia
        tutorDateTextView.text = currentTutor?.date.toString()
        tutorLocationTextView.text = currentTutor?.location
        tutorDescriptionTextView.text= currentTutor?.description

        // Load event image from URL using AsyncTask
        if (!currentTutor?.imageUrl.isNullOrEmpty()) {
            val imageLoader = EventImageLoader(tutorImageView)
            imageLoader.execute(currentTutor?.imageUrl)
        } else {
            // Set a placeholder image if there's no URL
            tutorImageView.setImageResource(R.drawable.logo)
        }

        return listItemView
    }

    private inner class EventImageLoader(private val imageView: ImageView) : AsyncTask<String, Void, Bitmap?>() {

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