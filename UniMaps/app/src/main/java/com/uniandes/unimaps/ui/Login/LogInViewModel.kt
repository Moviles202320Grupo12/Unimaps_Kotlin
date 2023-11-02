package com.uniandes.unimaps.ui.Login
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.uniandes.unimaps.asynctasks.DBAsyncTask
import com.uniandes.unimaps.models.UserModel
import kotlinx.coroutines.tasks.await

class LogInViewModel : ViewModel() {

    private var conexionBD = DBAsyncTask();

    suspend fun verifyUserLogIn (email: String, password: String): MutableList<UserModel> {
        val puedeIngresar = conexionBD.verifyUserLogIn(email, password);
        return puedeIngresar;
    }
}