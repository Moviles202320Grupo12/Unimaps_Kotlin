package com.uniandes.unimaps.ui.Login


import androidx.lifecycle.ViewModel
import com.uniandes.unimaps.asynctasks.DBAsyncTask
import com.uniandes.unimaps.models.UserModel

class LogInViewModel : ViewModel() {

    companion object {
        val LOG_IN_WAY_GOOGLE: String = "Google"
        val LOG_IN_WAY_EMAIL: String = "Email Auth";
    }

    private var conexionBD = DBAsyncTask();

    suspend fun verifyUserLogIn (email: String, password: String): MutableList<UserModel> {
        val puedeIngresar = conexionBD.verifyUserLogIn(email, password);
        return puedeIngresar;
    }

    suspend fun storeAndroidVersion (androidVersion: String) {
        conexionBD.storeAndroidVersion(androidVersion);
    }

    fun updateWaysLogIn(login_form: String)
    {
        conexionBD.updateWaysLogIn(login_form)
    }

    suspend fun storeTimeToLogIn(timeInMillis: Long) {
        conexionBD.storeTimeToLogIn(timeInMillis);
    }

}