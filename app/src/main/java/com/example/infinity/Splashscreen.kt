package com.example.infinity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.infinity.Utils.Utils
import com.example.infinity.admin.admin
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
    private lateinit var videoView: VideoView
    private lateinit var passButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splashscreen)

        videoView = findViewById(R.id.videoView)
        passButton = findViewById(R.id.skipButton)

        playFirstVideo()

        auth = Firebase.auth
    }

    private fun playFirstVideo() {
        val uri1 = Uri.parse("android.resource://${packageName}/${R.raw.animmy}")
        videoView.setVideoURI(uri1)
        videoView.start()

        videoView.setOnCompletionListener {
            playSecondVideo()
        }
    }

    private fun playSecondVideo() {
        val uri2 = Uri.parse("android.resource://${packageName}/${R.raw.videoanimy}")  // Replace with your second video resource
        videoView.setVideoURI(uri2)
        videoView.start()

        // Show the "Pass" button during the second video
        passButton.visibility = View.VISIBLE

        passButton.setOnClickListener {
            videoView.stopPlayback()  // Stop the second video
            proceedAfterVideo()       // Proceed with the app flow
        }

        videoView.setOnCompletionListener {
            // Hide the "Pass" button when the video finishes
            passButton.visibility = View.GONE
            proceedAfterVideo()
        }
    }

    private fun proceedAfterVideo() {
        checkUserAuthStatus()
    }

    private fun checkUserAuthStatus() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            liredata()
        } else {
            navigateToSignup()
        }
    }

    private fun navigateToSignup() {
        val intent = Intent(this@Splashscreen, SignupActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun gohome() {
        val intent = Intent(this@Splashscreen, home::class.java)
        startActivity(intent)
        finish()
    }
    fun goAdmin(){
        val i=Intent(this@Splashscreen, admin::class.java)
        startActivity(i)

    }


    private fun liredata() {
        val database = Firebase.database
        val userID = Firebase.auth.currentUser!!.uid
        val myRef = database.getReference("Users").child(userID)
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
                // Handle error
            }
        })
    }
    
}
