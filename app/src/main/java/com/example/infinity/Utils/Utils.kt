package com.example.infinity.Utils

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.example.infinity.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database


object Utils {
    val database = Firebase.database
    val productref = Firebase.database.getReference("product")
    val userRef = database.getReference("Users")
    val cartref = database.getReference("Cart").child(Firebase.auth.currentUser!!.uid)
    val orderRef = Firebase.database.getReference("Orders")
    var USER : User? = null



    fun opengallery(launcher: ActivityResultLauncher<Intent>){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        launcher.launch(intent)
    }
}