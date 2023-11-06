package com.uniandes.unimaps.ui.Tutor

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.text.SimpleDateFormat
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.uniandes.unimaps.R
import com.uniandes.unimaps.ui.Events.Event
import java.net.HttpURLConnection
import java.net.URL
import java.util.Date

class TutorInfoActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tutor_info)
        //actionbar
        val myToolbar: Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(myToolbar)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FED353")))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //set actionbar title
        myToolbar.title = "Detalle de Tutor"
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FED353")))
        val selectedTutor = intent.getParcelableExtra<Tutor>("selectedTutor")

        if (selectedTutor!=null){
            val tutorInfName= findViewById<TextView>(R.id.tutorInfoName)
            val tutorInfMateria =findViewById<TextView>(R.id.tutorInfoSpecialization)
            val tutorInfLocation =findViewById<TextView>(R.id.tutorInfoLocation)
            val tutorInfDesc=findViewById<TextView>(R.id.tutorInfoDescription)
            val tutorInfDate =findViewById<TextView>(R.id.tutorInfoDate)
            val tutorInfImage =findViewById<ImageView>(R.id.tutorInfoImage)
            tutorInfName.text=selectedTutor.name
            tutorInfDesc.text=selectedTutor.description
            tutorInfLocation.text=selectedTutor.location
            tutorInfMateria.text=selectedTutor.materia
            val date=selectedTutor.date
            if (date != null) {
                // Convert the Timestamp to a Date object
                val dateObj = Date(date.seconds * 1000) // Convert seconds to milliseconds

                // Format the date and time in "day/month/year hour:minute" format
                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
                val formattedDate = dateFormat.format(dateObj)
                tutorInfDate.text = formattedDate
            } else {
                // Handle the case when the date is null, e.g., set text to "Date not available"
                tutorInfDate.text = "Date not available"
            }

            val imageLoader = EventImageLoader(tutorInfImage)
            imageLoader.execute(selectedTutor?.imageUrl)

        }else{
            Log.d("TAG","The parsing of the selected tutor failed")

        }
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