package com.example.infinity.admin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infinity.R
import com.example.infinity.Utils.Utils.productref
import com.example.infinity.adapter.ProductAdapter
import com.example.infinity.databinding.FragmentAdminProductsBinding
import com.example.infinity.model.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue


class AdminProductsFragment : Fragment() {

    lateinit var binding: FragmentAdminProductsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdminProductsBinding.inflate(layoutInflater)
        val view = binding.root
        binding.plus.setOnClickListener {
            val i= Intent(requireContext(),addproduct::class.java)
            startActivity(i)

        }
        binding.productslist.layoutManager = LinearLayoutManager(requireContext())

        productref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val dataSnapshot = dataSnapshot.children
                val products = ArrayList<Product>()
                dataSnapshot.forEach { product ->
                    products.add(product.getValue<Product>()!!)

                }
                binding.progressBar3.visibility = View.GONE
                binding.productslist.adapter = ProductAdapter(requireContext(),products)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })














        return view
    }

}