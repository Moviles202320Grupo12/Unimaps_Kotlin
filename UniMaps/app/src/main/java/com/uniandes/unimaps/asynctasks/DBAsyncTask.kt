package com.uniandes.unimaps.asynctasks

import android.util.ArrayMap
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.uniandes.unimaps.models.UserModel
import com.uniandes.unimaps.ui.Events.Event
import com.uniandes.unimaps.ui.Tutor.Tutor
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

    /**
     * Función que trae todos los eventos desde la BD.
     */
    suspend fun getEventsCollection (): ArrayMap<String, Event> {
        val eventMap = ArrayMap<String, Event>()

        try {
            db.collection("events")
                .get().addOnSuccessListener { result ->
                    try {
                        for (document in result) {
                            val event = document.toObject(Event::class.java)
                            event.setId(document.id);
                            eventMap[event.getId()] = event
                            Log.d("TAG EVENTOS EXITO", "${document.id} => ${document.data}")
                        }
                    }
                    catch (exception: Exception)
                    {
                        Log.w("TAG EVENTOS ERROR", "Error saving documents: ", exception)
                    }

                }
                .addOnFailureListener { exception ->
                    Log.w("TAG EVENTOS ERROR", "Error getting documents: ", exception)
                }
                .await()

        } catch (exception: Exception) {
            Log.e("TAG", "Error en la consulta: ${exception.message}")
            throw exception
        }

        return eventMap;
    }

    suspend fun updateWalkingPoints(points: Int) {
        try {
            val db = FirebaseFirestore.getInstance()
            val data = hashMapOf("points" to points)

            // Realiza la operación de escritura en Firestore
            db.collection("walking_points")
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

    suspend fun verifyUserRegister (email: String): Boolean {
        var canRegister = false;
        try {
            val snapshot = db.collection("users")
                .whereEqualTo("email", email.trim())
                .get()
                .await()

            if (snapshot.isEmpty) {
                canRegister = true;
            } else {
                Log.w("TAG", "No se encontraron documentos que coincidan")
            }
        } catch (exception: Exception) {
            Log.e("TAG", "Error en la consulta: ${exception.message}")
            throw exception
        }
        return canRegister;
    }

    /**
     * Función que
     */
    suspend fun registerNewUSer (user: UserModel): Boolean {
        var registroConExito = false;
        try {
            db.collection("users")
                .add(user)
                .addOnSuccessListener { documentReference ->
                    // La operación de guardado fue exitosa
                    registroConExito = true
                    Log.d("TAG", "Usuario registrdo con ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    // Ocurrió un error al intentar guardar el documento
                    Log.w("TAG", "Error al guardar el documento", e)
                }.await()
        } catch (exception: Exception) {
            Log.e("TAG", "Error en la consulta: ${exception.message}")
            throw exception
        }
        return registroConExito;
    }

    suspend fun getTutorCollection(): ArrayMap<String, Tutor> {
        val tutorMap = ArrayMap<String, Tutor>()

        try {
            db.collection("tutors")
                .get().addOnSuccessListener { result ->
                    try {
                        for (document in result) {
                            val tutor = document.toObject(Tutor::class.java)
                            tutor.setId(document.id);
                            tutorMap[tutor.getId()] = tutor
                            Log.d("TAG tutor EXITO", "${document.id} => ${document.data}")
                        }
                    }
                    catch (exception: Exception)
                    {
                        Log.w("TAG tutor ERROR", "Error saving documents: ", exception)
                    }

                }
                .addOnFailureListener { exception ->
                    Log.w("TAG tutor ERROR", "Error getting documents: ", exception)
                }
                .await()

        } catch (exception: Exception) {
            Log.e("TAG", "Error en la consulta: ${exception.message}")
            throw exception
        }

        return tutorMap;
    }


}