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
        FirebaseApp.initializeApp(this)

        super.onCreate(savedInstanceState)
        binding = HomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth
    //binding.logout.setOnClickListener{  Firebase.auth.signOut() gosignup() }
        val frags = ArrayList<Fragment>()
        frags.add(HomeFrag())
        frags.add(CartFrag())
        frags.add(OrdersFrag())
        frags.add(AcountFrag())

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,frags[0]).commit()

        binding.fraghome.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container,frags[0]).commit()
        }
        binding.fragbuy.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container,frags[1]).commit()
        }
        binding.fraglivraison.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container,frags[2]).commit()
        }
        binding.fraguser.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container,frags[3]).commit()
        }







    }
    fun gosignup(){
        val i=Intent(this@home, SignupActivity::class.java)
        startActivity(i)
    }
    fun liredata(){
        val database = Firebase.database
        val UserID = auth.currentUser!!.uid
        val myRef = database.getReference("Users").child(UserID).child("name")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val username = dataSnapshot.getValue<String>()
               // binding.welcom.text = "welcome $username"
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}