package com.example.infinity.user

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infinity.adapter.NewsUserAdapter
import com.example.infinity.databinding.AnimynewsBinding
import com.example.infinity.model.News
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AnimyNews<View> : AppCompatActivity() {

    private lateinit var binding: AnimynewsBinding
    private lateinit var allNews: ArrayList<News>  // List to hold all news items
    private var currentSelectedCard: View? = null  // Keep track of the currently selected card

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        binding = AnimynewsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.backnews.setOnClickListener{
            startActivity(Intent(this, home::class.java))
            finish()
        }

        // Load news data
        loadNews()
    }

    private fun loadNews() {
        // Initialize Firebase reference for news
        val newsRef = Firebase.database.getReference("news")

        newsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                allNews = ArrayList()
                snapshot.children.forEach { newsChild ->
                    val news = newsChild.getValue<News>()
                    news?.let { allNews.add(it) }
                }
                // Show all news initially
                updateRecyclerView(allNews)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

    private fun updateRecyclerView(newsList: List<News>) {
        val adapter = NewsUserAdapter(this, ArrayList(newsList))
        binding.newslist.adapter = adapter
        binding.newslist.layoutManager = LinearLayoutManager(this)
    }
}

