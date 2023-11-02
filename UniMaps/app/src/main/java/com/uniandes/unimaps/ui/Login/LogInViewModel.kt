package com.uniandes.unimaps.ui.Login


import androidx.lifecycle.ViewModel
import com.uniandes.unimaps.asynctasks.DBAsyncTask
import com.uniandes.unimaps.models.UserModel

class LogInViewModel : ViewModel() {

    private var conexionBD = DBAsyncTask();

    suspend fun verifyUserLogIn (email: String, password: String): MutableList<UserModel> {
        val puedeIngresar = conexionBD.verifyUserLogIn(email, password);
        return puedeIngresar;
    }

    suspend fun storeAndroidVersion (androidVersion: String) {
        conexionBD.storeAndroidVersion(androidVersion);
    }

}