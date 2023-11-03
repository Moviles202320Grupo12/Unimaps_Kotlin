package com.uniandes.unimaps.asynctasks

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.uniandes.unimaps.models.UserModel
import kotlinx.coroutines.tasks.await

/**
 * Clase que representa la conexion a la base de datos.
 * Todas las operaciones sobre la BD se realizan de forma asincrona.
 */
class DBAsyncTask {

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


    /**
     * Función asincrona de verificacion de log in con info desde base de datos.
     */
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

    /**
     * Función asincrona para almacenar info de la BQ con version del OS.
     */
    suspend fun storeAndroidVersion(androidVersion: String) {
        try {
            val db = FirebaseFirestore.getInstance()
            val data = hashMapOf("version" to androidVersion)

            // Realiza la operación de escritura en Firestore
            db.collection("android_version")
                .document()
                .set(data)
                .await()

            // La operación se ha completado con éxito
            // Puedes agregar cualquier otro código de manejo aquí
        } catch (exception: Exception) {
            // Manejo de errores, como registro de errores
            Log.e("TAG", "Error en la consulta: ${exception.message}")
            // Puedes lanzar una excepción o manejarla según tus necesidades
        }
    }




}