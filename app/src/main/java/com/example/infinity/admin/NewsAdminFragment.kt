package com.example.infinity.admin


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infinity.adapter.NewsAdapter
import com.example.infinity.databinding.FragmentNewsAnimyBinding
import com.example.infinity.model.News
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class NewsAdminFragment : Fragment() {

    private lateinit var binding: FragmentNewsAnimyBinding
    private lateinit var newsRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNewsAnimyBinding.inflate(inflater, container, false)

        // Initialize Firebase reference
        newsRef = Firebase.database.getReference("news")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the RecyclerView
        binding.Newslist.layoutManager = LinearLayoutManager(requireContext())

        // Set up the click listener for adding news
        binding.plus.setOnClickListener {
            val intent = Intent(requireContext(), addnews::class.java)
            startActivity(intent)
        }

        // Show the progress bar while loading data
        binding.progressBar3.visibility = View.VISIBLE

        // Load news data
        loadNews()
    }

    private fun loadNews() {
        newsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isAdded) {  // Ensure the fragment is still attached to its activity
                    val newsList = ArrayList<News>()
                    for (newsSnapshot in snapshot.children) {
                        val news = newsSnapshot.getValue(News::class.java)
                        news?.let { newsList.add(it) }
                    }
                    // Hide the progress bar and set the adapter
                    binding.progressBar3.visibility = View.GONE
                    binding.Newslist.adapter = NewsAdapter(requireContext(), newsList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                if (isAdded) {  // Ensure the fragment is still attached to its activity
                    binding.progressBar3.visibility = View.GONE
                    Toast.makeText(requireContext(), "Failed to load news", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}


