package com.uniandes.unimaps.ui.Feed

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.uniandes.unimaps.EventsFeedActivity
import com.uniandes.unimaps.MainActivity
import com.uniandes.unimaps.R
import com.uniandes.unimaps.RegisterActivity
import com.uniandes.unimaps.databinding.FragmentFeedBinding
import com.uniandes.unimaps.ui.WalkingPoints.WalkingFragment
import com.uniandes.unimaps.ui.home.HomeFragment

class FeedFragment : Fragment() {

    private var _binding: FragmentFeedBinding? = null

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
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.arribaFeed, WalkingFragment())
            transaction?.disallowAddToBackStack()
            transaction?.commit()

        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}