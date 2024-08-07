package com.example.infinity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.infinity.databinding.ForgetpasswordBinding
import com.example.infinity.databinding.HomeBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

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
                        val i=Intent(this@forgetpassword,login::class.java)
                        startActivity(i)
                    }
                    
                }
        }







    }
    }