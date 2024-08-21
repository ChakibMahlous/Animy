package com.example.infinity.user

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.infinity.Utils.Utils
import com.example.infinity.admin.admin
import com.example.infinity.databinding.LoginBinding
import com.example.infinity.model.User
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

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
        val password=binding.Pasword2.text.toString()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this,"welcome back",Toast.LENGTH_LONG).show()
                     liredata()
                    //gohome()

                } else {
                    Toast.makeText(this,task.exception!!.message,Toast.LENGTH_LONG).show()

                }
            }

    }
    fun gohome(){
        val i=Intent(this@login,home::class.java)
        startActivity(i)
    }
    fun goAdmin(){
        val i=Intent(this@login,admin::class.java)
        startActivity(i)

    }
    fun gochangeit(){
        val i=Intent(this@login, forgetpassword::class.java)
        startActivity(i)
    }
    fun liredata(){
        val database = Firebase.database
        val UserID = Firebase.auth.currentUser!!.uid
        val myRef = database.getReference("Users").child(UserID)
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                Utils.USER = dataSnapshot.getValue<User>()
                if(Utils.USER!!.admin!=null){
                    if (Utils.USER!!.admin!!){
                        goAdmin()
                    }else{
                        gohome()

                    }
                }else{
                    gohome()
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}