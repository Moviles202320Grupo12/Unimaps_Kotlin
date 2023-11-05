package com.uniandes.unimaps

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.uniandes.unimaps.databinding.RegistroBinding
import com.uniandes.unimaps.ui.Login.LogInViewModel
import com.uniandes.unimaps.ui.register.RegisterViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class RegisterActivity : AppCompatActivity() {
    // Esto hace el binding con la vista xml de Registro:
    private lateinit var binding: RegistroBinding

    private lateinit var registerViewModel: RegisterViewModel

    // Variable de Firebase:
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar auth de Firebase:
        auth = FirebaseAuth.getInstance()

        // Inicializar el ViewModel
        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = ""
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#000000")))
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        binding = RegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.registerbutton.setOnClickListener(View.OnClickListener {
            // Registra nuevo usuario
            val fullName = binding.fullName.text.toString()
            val username = binding.username.text.toString()
            val phoneNumber = binding.phoneNumber.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val passwordConfirm = binding.confirmPassword.text.toString()
            if(fullName != "" && username != "" && phoneNumber != "" && email != "" && password!= "" && passwordConfirm!= "")
            {
                if(password == passwordConfirm)
                {
                    // Inicializar el contexto de CoroutineScope:
                    val coroutineScope = CoroutineScope(Dispatchers.Main)

                    // Llamar a la función verifyUserLogIn en un contexto de Coroutine:
                    coroutineScope.launch {
                        try {
                            val puedeRegistrarse = registerViewModel.verifyUserRegister(email)
                            if(puedeRegistrarse)
                            {
                                if(registerViewModel.registerNewUSer(fullName, username, phoneNumber, email, password))
                                {
                                    createAccount(email, password)
                                    val intent = Intent(this@RegisterActivity, AccessActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                else
                                {
                                    val toast = Toast.makeText(applicationContext, "Ocurio un error en el registro!", Toast.LENGTH_LONG)
                                    toast.show()
                                }
                            }
                            else
                            {
                                val toast = Toast.makeText(applicationContext, "Ya existe un usuario registrado con este email!", Toast.LENGTH_LONG)
                                toast.show()
                            }

                        }
                        catch  (exception: Exception)
                        {
                            Log.e("TAG", "Error en la consulta: ${exception.message}")
                            val toast = Toast.makeText(applicationContext, "Error al conectar con la BD", Toast.LENGTH_LONG)
                            toast.show()
                        }
                    }
                }
                else{
                    val toast = Toast.makeText(applicationContext, "Las contraseñas no coinciden", Toast.LENGTH_LONG)
                    toast.show()
                }
            }
            else
            {
                val toast = Toast.makeText(applicationContext, "Por favor completa todos los campos para regitrarte", Toast.LENGTH_LONG)
                toast.show()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /**
     * Función asincrona que crea una nueva cuenta en Firebase auth con la info nuevo usuario.
     */
    private suspend fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registro exitoso, puedes redirigir al usuario a la actividad principal o realizar otras acciones
                    val user: FirebaseUser? = auth.currentUser
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    // Agregar aquí la lógica para redirigir al usuario
                } else {
                    Log.w("TAG ERROR REGISTRO", "Error al crear cuenta de usuario: ${email}, Error: ${task.exception}")
                    // Error en el registro, muestra un mensaje de error al usuario
                    Toast.makeText(this, "Ocurrio un error en el registro: ${task.exception?.message}}", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener{exception ->
                Log.w("TAG ERROR REGISTRO", "Error al crear cuenta de usuario: ${email}, Error: ${exception.message}")
                Toast.makeText(this, "Error en el registro!", Toast.LENGTH_LONG).show()
            }.await()
    }
}