package com.example.infinity.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.infinity.R

class NewsViewHolder (itemView : View): RecyclerView.ViewHolder(itemView){
    val newsdescription  = itemView.findViewById<TextView>(R.id.desciptionnews)
    val newstitle  = itemView.findViewById<TextView>(R.id.Newstitle)
    val newsImage : ImageView = itemView.findViewById(R.id.prdctimgnewsrow)
}