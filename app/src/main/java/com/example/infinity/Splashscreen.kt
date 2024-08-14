package com.example.infinity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.infinity.Utils.Utils
import com.example.infinity.model.User
import com.example.infinity.user.SignupActivity
import com.example.infinity.user.home
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

class Splashscreen : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splashscreen)
        val videoView = findViewById<VideoView>(R.id.videoView!!)
        val packageName = "android.resource://" + getPackageName() + "/" + R.raw.animmy
        val uri = Uri.parse(packageName)
        videoView.setVideoURI(uri)
        videoView.start()
        auth  = Firebase.auth
    }

    public override fun onStart() {
        auth = FirebaseAuth.getInstance()

        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            liredata()
        }else{
            val thread = object : Thread(){
                override fun run() {
                    try {
                        sleep(2000)
                    }catch (e:Exception){
                        e.printStackTrace()
                    }finally {
                        val i= Intent(this@Splashscreen,SignupActivity::class.java)
                        startActivity(i)
                        finish()
                    }
                }
            }
            thread.start()
        }
    }

    fun gohome(){
        val i= Intent(this@Splashscreen, home::class.java)
        startActivity(i)
        finish()
    }
    fun liredata(){
        val database = Firebase.database
        val UserID = Firebase.auth.currentUser!!.uid
        val myRef = database.getReference("Users").child(UserID)
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                 Utils.USER = dataSnapshot.getValue<User>()
                gohome()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}