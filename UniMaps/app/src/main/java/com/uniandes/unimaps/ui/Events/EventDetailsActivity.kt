package com.uniandes.unimaps.ui.Events

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.uniandes.unimaps.ui.Events.Event
import com.uniandes.unimaps.databinding.ActivityEventDetailsBinding  // Replace 'com.yourpackage' with your actual package name
class EventDetailsActivity : AppCompatActivity()  {

    private lateinit var binding: ActivityEventDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEventDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Get the event data passed from EventFeedActivity

    }

}