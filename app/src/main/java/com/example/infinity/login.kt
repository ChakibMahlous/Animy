package com.example.infinity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.infinity.admin.admin
import com.example.infinity.databinding.ActivityMainBinding
import com.example.infinity.databinding.LoginBinding
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class login : AppCompatActivity() {
    lateinit var binding: LoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)

        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth


        binding.login.setOnClickListener{
            login()
        }
        binding.changeit.setOnClickListener{
            gochangeit()
        }









    }
    fun login(){
        val email =binding.email2.text.toString()
        val password=binding.password2.text.toString()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this,"welcome back",Toast.LENGTH_LONG).show()
                    gohome()

                } else {
                    Toast.makeText(this,task.exception!!.message,Toast.LENGTH_LONG).show()

                }
            }

    }
    fun gohome(){
        val i=Intent(this@login,admin::class.java)
        startActivity(i)
    }
    fun gochangeit(){
        val i=Intent(this@login,forgetpassword::class.java)
        startActivity(i)
    }
}