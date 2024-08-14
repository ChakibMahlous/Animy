package com.example.infinity.user

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.infinity.databinding.ForgetpasswordBinding
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class forgetpassword : AppCompatActivity() {
    lateinit var binding: ForgetpasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)

        super.onCreate(savedInstanceState)
        binding = ForgetpasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth
        binding.change.setOnClickListener{
            Firebase.auth.sendPasswordResetEmail(binding.email3.text.toString())
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful){
                        Toast.makeText(this, "email send check your inbox", Toast.LENGTH_LONG).show()
                        val i=Intent(this@forgetpassword, login::class.java)
                        startActivity(i)
                    }
                    
                }
        }







    }
    }