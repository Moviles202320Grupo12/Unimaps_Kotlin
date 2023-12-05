package com.uniandes.unimaps.ui.Tutor

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.AsyncTask
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
            val tutorInfDate =findViewById<TextView>(R.id.tutorTimeDate)
            val tutorInfImage =findViewById<ImageView>(R.id.tutorInfoImage)
            val tutorInfBusquedas =findViewById<TextView>(R.id.tutorInfoBusquedas)
            val tutorinfPhone =findViewById<TextView>(R.id.tutorInfPhone)
            val whatsButton =findViewById<Button>(R.id.tutorInfoRequestButton)
            tutorInfName.text=selectedTutor.name
            tutorInfDesc.text=selectedTutor.description
            tutorInfLocation.text=selectedTutor.location
            tutorInfMateria.text=selectedTutor.materia
            tutorinfPhone.text=selectedTutor.phone
            val phone =selectedTutor.phone
            supportActionBar!!.title=tutorInfName.text
            val date=selectedTutor.date
            val bus=selectedTutor.numBusquedas
            tutorInfBusquedas.text=bus
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

            whatsButton.setOnClickListener{
                if (!phone.isNullOrBlank()) {
                    openWhatsAppChat(phone)
                } else {
                    // Handle the case when the phone number is not available

                    Toast.makeText(this,"\"Phone number not available\"", Toast.LENGTH_SHORT).show()
                }

            }
        }else{
            Log.d("TAG","The parsing of the selected tutor failed")

        }
    }

    private fun openWhatsAppChat(phoneNumber: String) {
        try {
            val intent = Intent("android.intent.action.MAIN")
            intent.action = Intent.ACTION_VIEW
            intent.setPackage("com.whatsapp")
            val colPhone="+57$phoneNumber"
            // Use Uri.encode to handle special characters in the phone number
            val data = Uri.parse("https://wa.me/${Uri.encode(colPhone)}")
            intent.data = data
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this,"WhatsApp is not installed on your device", Toast.LENGTH_SHORT).show()
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