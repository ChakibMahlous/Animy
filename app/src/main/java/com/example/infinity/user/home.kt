package com.example.infinity.user

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.infinity.R
import com.example.infinity.databinding.HomeBinding
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
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        binding = HomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth

        // Set up initial fragment
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFrag()).commit()

        // Set up BottomNavigationView
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFrag()).commit()
                    true
                }
                R.id.navigation_buy -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, CartFrag()).commit()
                    true
                }
                R.id.navigation_livraison -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, OrdersFrag()).commit()
                    true
                }
                R.id.navigation_user -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, AcountFrag()).commit()
                    true
                }
                else -> false
            }
        }
    }

    private fun goSignup() {
        val i = Intent(this@home, SignupActivity::class.java)
        startActivity(i)
    }

    private fun readUserData() {
        val database = Firebase.database
        val userID = auth.currentUser!!.uid
        val myRef = database.getReference("Users").child(userID).child("name")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val username = dataSnapshot.getValue<String>()
                // binding.welcom.text = "welcome $username"
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}
