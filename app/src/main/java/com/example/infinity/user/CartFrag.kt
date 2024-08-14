package com.example.infinity.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infinity.R
import com.example.infinity.Utils.Utils
import com.example.infinity.adapter.CartAdapter
import com.example.infinity.databinding.FragmentCartBinding
import com.example.infinity.model.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class CartFrag : Fragment() {
    companion object{
        @JvmStatic
        lateinit var binding: FragmentCartBinding

        @JvmStatic
        lateinit var PRODUCTS : ArrayList<Product>

        @JvmStatic
        var TOTAL_PRICE = 0.0


        @JvmStatic
        fun calctotalprice(){
            var TOTAL_PRICE = 0.0

            PRODUCTS.forEach{ product ->
                TOTAL_PRICE += product.price.toDouble()
            }
            binding.totalprice.text = "$TOTAL_PRICE DZD"

        }

    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentCartBinding.inflate(layoutInflater)
         val  view = binding.root
        retrieveCartProducts()


        binding.purchase.setOnClickListener {
            if (PRODUCTS.isNotEmpty()){
                startActivity(Intent(activity,ConfirmOrderActivity::class.java)
                    .putExtra("total_price", binding.totalprice.text.toString()))

            }else{
                Toast.makeText(requireContext(),"Add products first",Toast.LENGTH_SHORT).show()
            }
        }





        return view
    }
    fun retrieveCartProducts(){
        binding.progressBar4.visibility = View.VISIBLE
        Utils.cartref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                PRODUCTS=ArrayList()
                val productsChildren = snapshot.children
                productsChildren.forEach { productSnapshot ->
                    PRODUCTS.add(productSnapshot.getValue<Product>()!!)
                    binding.prdctrecyclerviewpannier.layoutManager = LinearLayoutManager (requireContext())
                    binding.prdctrecyclerviewpannier.adapter = CartAdapter(requireContext(), PRODUCTS)

                }
                binding.progressBar4.visibility = View.GONE
                calctotalprice()
                binding.prdctrecyclerviewpannier.adapter = CartAdapter(requireContext(), PRODUCTS)
                if (PRODUCTS.isEmpty()){
                    binding.cartempty.visibility = View.VISIBLE
                }else{
                    binding.cartempty.visibility = View.GONE

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }


}