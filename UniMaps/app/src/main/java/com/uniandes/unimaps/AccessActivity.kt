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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.uniandes.unimaps.databinding.AccessBinding
import com.uniandes.unimaps.ui.Login.LogInViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AccessActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    // Esto hace el binding con la vista xml de Registro:
    private lateinit var binding: AccessBinding

    private lateinit var loginViewModel: LogInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //
        mAuth = FirebaseAuth.getInstance()

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = ""
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#000000")))
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        // Inicializar el ViewModel
        loginViewModel = ViewModelProvider(this).get(LogInViewModel::class.java)

        binding = AccessBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginbutton.setOnClickListener(View.OnClickListener {
            // Se inicia sesion:
            // Manda a ventana principal:
            val username = binding.email.text.toString()
            val password = binding.password.text.toString()
            if(username != "" && password!= "")
            {
                /**
                 * Se utilizan corutinas para la parte del manejo de concurrencia de la aplicación:
                 */

                // Inicializar el contexto de CoroutineScope:
                val coroutineScope = CoroutineScope(Dispatchers.Main)

                // Llamar a la función verifyUserLogIn en un contexto de Coroutine:
                coroutineScope.launch {
                    try {
                        val puedeIngresar = loginViewModel.verifyUserLogIn(username, password)
                        if(puedeIngresar.isNotEmpty())
                        {
                            signInWithEmailAndPassword(username, password)
                        }
                        else
                        {
                            val toast = Toast.makeText(applicationContext, "Usuario Errado!", Toast.LENGTH_LONG)
                            toast.show()
                        }

                    }
                    catch  (e: Exception)
                    {
                        val toast = Toast.makeText(applicationContext, "Error al conectar con la BD", Toast.LENGTH_LONG)
                        toast.show()
                    }
                }
            }

            else{
                val toast = Toast.makeText(applicationContext, "Por favor rellene todos los campos", Toast.LENGTH_LONG)
                toast.show()
            }

        })

        // Configurar Google Sign - In:
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.googleAccess.setOnClickListener{
            mGoogleSignInClient.signOut()
            startActivityForResult(mGoogleSignInClient.signInIntent, 13)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 13 && resultCode== RESULT_OK)
        {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken!!)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val intent = Intent(this@AccessActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@AccessActivity, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener{
                val toast = Toast.makeText(applicationContext, "Error de autenticacion con Google", Toast.LENGTH_LONG)
                toast.show()
            }

    }


    private fun signInWithEmailAndPassword(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso, puedes redirigir al usuario a la actividad principal o realizar otras acciones
                    val intent = Intent(this@AccessActivity, MainActivity::class.java)
                    startActivity(intent)
                    // Agregar aquí la lógica para redirigir al usuario
                } else {
                    // Error en el inicio de sesión, muestra un mensaje de error al usuario
                    Toast.makeText(this, "Error en el inicio de sesión. Verifica tus credenciales.", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener{
                val toast = Toast.makeText(applicationContext, "Error de autenticacion con contraseña", Toast.LENGTH_LONG)
                toast.show()
            }
    }




}