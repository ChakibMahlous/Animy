package com.example.infinity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.infinity.databinding.HomeBinding
import com.example.infinity.databinding.LoginBinding
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

class home : AppCompatActivity() {
    lateinit var binding: HomeBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)

        super.onCreate(savedInstanceState)
        binding = HomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth


        binding.logout.setOnClickListener{
            Firebase.auth.signOut()
            gosignup()
        }
        liredata()









    }
    fun gosignup(){
        val i=Intent(this@home,MainActivity::class.java)
        startActivity(i)
    }
    fun liredata(){
        val database = Firebase.database
        val UserID = auth.currentUser!!.uid
        val myRef = database.getReference("Users").child(UserID).child("name")
         //YO
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val username = dataSnapshot.getValue<String>()
                binding.welcom.text = "welcome $username"
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}