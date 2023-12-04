package com.uniandes.unimaps.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.uniandes.unimaps.MainActivity
import com.uniandes.unimaps.R
import com.uniandes.unimaps.databinding.FragmentLogOutBinding

class LogOutFragment : Fragment() {

    private var _binding: FragmentLogOutBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Aquí puedes realizar la lógica de cierre de sesión
        // Por ejemplo, eliminar las credenciales de usuario, cerrar sesión en servicios, etc.
        _binding = FragmentLogOutBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Luego, puedes navegar a la pantalla de inicio de sesión
        logout()

        return inflater.inflate(R.layout.fragment_log_out, container, false)
    }

    private fun logout() {
        val yourActivity = activity as? MainActivity
        yourActivity?.logout()
    }
}


