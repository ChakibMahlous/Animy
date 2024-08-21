package com.example.infinity.admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.R
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infinity.adapter.ProductAdapter
import com.example.infinity.databinding.AdminBinding
import com.example.infinity.model.Product
import com.example.infinity.user.AcountFrag
import com.example.infinity.user.CartFrag
import com.example.infinity.user.HomeFrag
import com.example.infinity.user.OrdersFrag
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

class admin : AppCompatActivity() {
    lateinit var binding: AdminBinding
    private lateinit var auth: FirebaseAuth
    lateinit var productref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)

        super.onCreate(savedInstanceState)
        binding = AdminBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        productref = Firebase.database.getReference("product")
        auth = Firebase.auth

        supportFragmentManager.beginTransaction().replace(com.example.infinity.R.id.fragment_container, AdminProductsFragment()).commit()

        binding.adminBottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                com.example.infinity.R.id.navigation_listadmin -> {
                    supportFragmentManager.beginTransaction().replace(com.example.infinity.R.id.fragment_container, AdminProductsFragment()).commit()
                    true
                }
                com.example.infinity.R.id.navigation_livraisonadmin -> {
                    supportFragmentManager.beginTransaction().replace(com.example.infinity.R.id.fragment_container, AdminOrdersFrag()).commit()
                    true
                }
                com.example.infinity.R.id.navigation_useradmin -> {
                    supportFragmentManager.beginTransaction().replace(com.example.infinity.R.id.fragment_container, AcountFrag()).commit()
                    true
                }
                else -> false
            }
        }
    }











}
