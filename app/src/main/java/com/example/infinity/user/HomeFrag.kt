package com.example.infinity.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infinity.R
import com.example.infinity.Utils.Utils
import com.example.infinity.adapter.UserProductAdapter
import com.example.infinity.databinding.FragmentHomeBinding
import com.example.infinity.model.Product
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.squareup.picasso.Picasso


class HomeFrag : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var allProducts: ArrayList<Product>  // List to hold all products
    private var currentSelectedCard: View? = null  // Keep track of the currently selected card

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        // Load all products initially
        loadProducts()

        // Set up user info
        binding.hellouser.text = "Welcome ${Utils.USER?.name}"
        Utils.USER?.prfilepic?.let {
            if (it.isNotEmpty()) {
                Picasso.get().load(it).into(binding.user)
            }
        }

        // Set up category filters
        setupCategoryFilters()

        return view
    }

    private fun loadProducts() {
        Utils.productref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                allProducts = ArrayList()
                snapshot.children.forEach { productChild ->
                    val product = productChild.getValue<Product>()
                    product?.let { allProducts.add(it) }
                }
                // Show all products initially
                updateRecyclerView(allProducts)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

    private fun updateRecyclerView(productList: List<Product>) {
        val adapter = UserProductAdapter(requireContext(), ArrayList(productList))
        binding.productrecyclerview.adapter = adapter
        binding.productrecyclerview.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupCategoryFilters() {
        binding.PosterCard.setOnClickListener {
            filterProductsByCategory("Poster", binding.PosterCard)
        }
        binding.FigurineCard.setOnClickListener {
            filterProductsByCategory("Figurine", binding.FigurineCard)
        }
        binding.ClothesCard.setOnClickListener {
            filterProductsByCategory("Clothes", binding.ClothesCard)
        }
        binding.EventCard.setOnClickListener {
            filterProductsByCategory("Event", binding.EventCard)
        }
        binding.OutilsCard.setOnClickListener {
            filterProductsByCategory("Outils", binding.OutilsCard)
        }
        binding.OthersCard.setOnClickListener {
            filterProductsByCategory("Others", binding.OthersCard)
        }
    }

    private fun filterProductsByCategory(category: String, selectedCard: View) {
        // Update the selected filter UI
        highlightSelectedFilter(selectedCard)

        // Filter products based on the category
        val filteredProducts = allProducts.filter { it.type == category }
        updateRecyclerView(filteredProducts)
    }

    private fun highlightSelectedFilter(selectedCard: View) {
        // Reset the background of the previously selected card
        currentSelectedCard?.setBackgroundResource(R.drawable.default_card_backgroung)

        // Highlight the selected card
        selectedCard.setBackgroundResource(R.drawable.selected_card_background)

        // Update the current selected card
        currentSelectedCard = selectedCard
    }
}
