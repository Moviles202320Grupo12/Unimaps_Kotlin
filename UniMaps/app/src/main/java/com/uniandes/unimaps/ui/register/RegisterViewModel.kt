package com.uniandes.unimaps.ui.register

import androidx.lifecycle.ViewModel
import com.uniandes.unimaps.asynctasks.DBAsyncTask
import com.uniandes.unimaps.models.UserModel

class RegisterViewModel: ViewModel() {

    private var conexionBD = DBAsyncTask();

    suspend fun verifyUserRegister (email: String): Boolean {
        val puedeRegistrarse = conexionBD.verifyUserRegister(email);
        return puedeRegistrarse;
    }

    suspend fun registerNewUSer (fullName: String, username: String, phoneNumber: String, email: String, password: String): Boolean {
        val newUSer = UserModel("", fullName,email, phoneNumber, password, username)
        val puedeRegistrarse = conexionBD.registerNewUSer(newUSer);
        return puedeRegistrarse;
    }


}