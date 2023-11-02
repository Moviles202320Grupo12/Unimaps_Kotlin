package com.uniandes.unimaps.ui.Login
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.uniandes.unimaps.models.UserModel
import kotlinx.coroutines.tasks.await

class LogInViewModel : ViewModel() {
    private var currentUser: UserModel? = null

    // Conexion a base de datos:
    private val db = Firebase.firestore

    // Método para verificar si el usuario está autenticado
    fun isUserAuthenticated(): Boolean {
        return currentUser != null
    }

    // Método para obtener el usuario actual después del inicio de sesión
    fun getCurrentUser(): UserModel? {
        return currentUser
    }

    // Método para cerrar sesión
    fun logout() {
        currentUser = null
    }

    suspend fun verifyUserLogIn (email: String, password: String): MutableList<UserModel> {
        val dataSet: MutableList<UserModel> = mutableListOf()

        try {
            val snapshot = db.collection("users")
                .whereEqualTo("email", email.trim())
                .whereEqualTo("password", password.trim())
                .get()
                .await()

            if (!snapshot.isEmpty) {
                for (document in snapshot) {
                    if (document.exists()) {
                        val usuario = document.toObject(UserModel::class.java)
                        currentUser = usuario
                        dataSet += usuario
                    }
                }
            } else {
                Log.w("TAG", "No se encontraron documentos que coincidan")
            }
        } catch (exception: Exception) {
            Log.e("TAG", "Error en la consulta: ${exception.message}")
            throw exception
        }

        return dataSet
    }
}