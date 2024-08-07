package com.example.infinity.admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infinity.adapter.ProductAdapter
import com.example.infinity.databinding.AdminBinding
import com.example.infinity.model.Product
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

        binding.plus.setOnClickListener {
            val i=Intent(this@admin,addproduct::class.java)
            startActivity(i)

        }
        binding.productslist.layoutManager = LinearLayoutManager(this)

        productref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val dataSnapshot = dataSnapshot.children
                val products = ArrayList<Product>()
                dataSnapshot.forEach { product ->
                    products.add(product.getValue<Product>()!!)

                }
                binding.progressBar3.visibility = View.GONE
                binding.productslist.adapter = ProductAdapter(this@admin,products)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })











    }
}