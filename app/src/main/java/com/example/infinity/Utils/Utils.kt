package com.example.infinity.Utils

import com.google.firebase.Firebase
import com.google.firebase.database.database


object Utils {
    val database = Firebase.database
    val productref = Firebase.database.getReference("product")
    val userRef = database.getReference("Users")

}