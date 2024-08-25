package com.example.infinity.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.infinity.R

class UserProductViewHolder (itemView : View): RecyclerView.ViewHolder(itemView){
    val productName = itemView.findViewById<TextView>(R.id.prdctnamerow)
    val productPrice = itemView.findViewById<TextView>(R.id.prdctpricerow)
    val productImage: ImageView = itemView.findViewById(R.id.prdctimgnewsrow)
    val producttype = itemView.findViewById<TextView>(R.id.producttyperow)
    val addtocart = itemView.findViewById<TextView>(R.id.addtocartrow)







}
