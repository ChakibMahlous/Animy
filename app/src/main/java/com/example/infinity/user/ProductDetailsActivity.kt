package com.example.infinity.user

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.infinity.R
import com.example.infinity.Utils.Utils
import com.example.infinity.databinding.ActivityProductDetailsBinding
import com.example.infinity.model.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.squareup.picasso.Picasso

class ProductDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityProductDetailsBinding
    lateinit var product_id : String
    lateinit var PRODUCT : Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        product_id = intent!!.extras!!.getString("product_id")!!
        binding.Back2.setOnClickListener{
            startActivity(Intent(this,home::class.java))
            finish()
        }
        Utils.productref.child(product_id).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                PRODUCT = snapshot.getValue<Product>()!!
                binding.productnam3.text = PRODUCT.name
                binding.productprice3.text =  "${PRODUCT.price} DZD"
                binding.producttype3.text = PRODUCT.type
                binding.prdctname3.text = PRODUCT.name
                binding.descriptionn3.text = PRODUCT.description
                Picasso.get().load(PRODUCT.prdctimg).into(binding.imageView7)

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        binding.addtocart3.setOnClickListener {
            Utils.cartref.child(product_id).setValue(PRODUCT)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        Toast.makeText(this,"${PRODUCT.name} added to cart", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this,task.exception!!.message, Toast.LENGTH_SHORT).show()

                    }

                }
        }



    }
}