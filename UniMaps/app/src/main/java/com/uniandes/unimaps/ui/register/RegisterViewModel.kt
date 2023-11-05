package com.uniandes.unimaps.ui.register

import androidx.lifecycle.ViewModel
import com.uniandes.unimaps.asynctasks.DBAsyncTask
import com.uniandes.unimaps.models.UserModel

class RegisterViewModel: ViewModel() {

    private var conexionBD = DBAsyncTask();

    /**
     * Función asincrona que verifica si el usuario puede registrarse o no. La forma de verificar es
     * revisar si ya existe un usuario con el correo ingresado en la BD.
     */
    suspend fun verifyUserRegister (email: String): Boolean {
        val puedeRegistrarse = conexionBD.verifyUserRegister(email);
        return puedeRegistrarse;
    }

    /**
     * Función asincrona que crea un usuario en la BD sin agregarlo aún a la auth de Firebase.
     */
    suspend fun registerNewUSer (fullName: String, username: String, phoneNumber: String, email: String, password: String): Boolean {
        val newUSer = UserModel(null, fullName,email, phoneNumber, password, username)
        val puedeRegistrarse = conexionBD.registerNewUSer(newUSer);
        return puedeRegistrarse;
    }


}