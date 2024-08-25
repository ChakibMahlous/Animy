package com.example.infinity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.infinity.databinding.RowNewsanimyBinding
import com.example.infinity.model.News
import com.squareup.picasso.Picasso
import android.widget.Toast

class NewsUserAdapter(
    private val context: Context,
    private val newsList: ArrayList<News>
) : RecyclerView.Adapter<NewsUserAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = RowNewsanimyBinding.inflate(LayoutInflater.from(context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList[position]
        holder.bind(news)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    class NewsViewHolder(private val binding: RowNewsanimyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news: News) {
            binding.Newsadmintitle.text = news.title
            binding.admindesciptionnews.text = news.description

            if (!news.imagenews.isNullOrEmpty()) {
                Picasso.get().load(news.imagenews).into(binding.adminimgnewsrow)
            } else {
                Toast.makeText(binding.root.context, "Image URL is missing", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
