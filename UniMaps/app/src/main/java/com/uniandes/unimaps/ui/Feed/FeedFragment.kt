package com.uniandes.unimaps.ui.Feed

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.uniandes.unimaps.ui.Events.EventsFeedActivity
import com.uniandes.unimaps.R
import com.uniandes.unimaps.RegisterActivity
import com.uniandes.unimaps.databinding.FragmentFeedBinding
import com.uniandes.unimaps.ui.Tutor.TutorsSearchActivity
import com.uniandes.unimaps.ui.WalkingPoints.WalkingFragment
import com.uniandes.unimaps.ui.home.HomeFragment

class FeedFragment : Fragment() {

    private var _binding: FragmentFeedBinding? = null
    private val startTime = SystemClock.elapsedRealtime()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val FeedViewModel =
            ViewModelProvider(this).get(FeedViewModel::class.java)

        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // Configurar Image View de Eventos:
        // Configurar Image View:
        val eventButton =root.findViewById<Button>(R.id.buttonEventFeed)
        eventButton.setOnClickListener {
            val intent = Intent (getActivity(), EventsFeedActivity::class.java)
            getActivity()?.startActivity(intent)

        }

        val tutorButton =root.findViewById<Button>(R.id.buttonTutorSearch)
        tutorButton.setOnClickListener {
            val intent = Intent (getActivity(), TutorsSearchActivity::class.java)
            getActivity()?.startActivity(intent)

        }

        val myImageView = root.findViewById<ImageView>(R.id.imageView9)
        myImageView.setOnClickListener {
            val intent = Intent (getActivity(), EventsFeedActivity::class.java)
            getActivity()?.startActivity(intent)
        }

        // Configurar Boton Lost Property:
        val lostPropertyButton = root.findViewById<Button>(R.id.button7)

        lostPropertyButton.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.arribaFeed, HomeFragment())
            transaction?.disallowAddToBackStack()
            transaction?.commit()
        }

        // Configurar Boton Walking Points:
        val walkingPointsButton = root.findViewById<Button>(R.id.button8)

        walkingPointsButton.setOnClickListener {
            val intent = Intent (getActivity(), WalkingFragment::class.java)
            getActivity()?.startActivity(intent)

        }
        return root
    }

    override fun onDestroyView() {
        // Obtenemos el tiempo actual en milisegundos.
        val endTime = SystemClock.elapsedRealtime()

        // Calculamos el tiempo que el usuario pasó en la aplicación.
        val timeInMillis = endTime - startTime

        // Imprimimos el tiempo en milisegundos.
        Log.i("Tiempo en la aplicación", timeInMillis.toString()+ " ms")
        super.onDestroyView()
        _binding = null
    }
}