package com.example.infinity.user

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.infinity.databinding.ActivityMainBinding
import com.example.infinity.model.User
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.database

class SignupActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth



        binding.signup.setOnClickListener{
            signup()
        }
        binding.login.setOnClickListener{
            val i= Intent(this@SignupActivity, login::class.java)
            startActivity(i)
        }

        val database = Firebase.database
        val myRef = database.getReference("Users").child("chakib").child("mh")

        myRef.setValue("one piece")





    }
     fun signup(){
        val email=binding.email.text.toString()
        val password= binding.password.text.toString()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    savedatatodb()
                    Toast.makeText(this,"account created",Toast.LENGTH_LONG).show()

                } else {
                    val message = task.exception!!.message
                    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
                }
            }
    }
     fun gohome(){
        val i=Intent(this@SignupActivity,home::class.java)
        startActivity(i)
         finish()
    }
    //public override fun onStart() {
//super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.hda cmnt t3 sah
      //  val currentUser = auth.currentUser
      //  if (currentUser != null) {
          //  gohome()
      //  }
   // }
    fun savedatatodb(){
        val database = Firebase.database
        val UserID = auth.currentUser!!.uid
        val myRef = database.getReference("Users").child(UserID)
        val user = User(binding.name.text.toString(),binding.phone.text.toString(),binding.email.text.toString(),
        null,false)

        myRef.setValue(user)



    }
}