package com.example.infinity.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.infinity.R

class prodectviewholder(itemView : View): RecyclerView.ViewHolder(itemView){
    val productName = itemView.findViewById<TextView>(R.id.prdctnamerow)
    val productPrice = itemView.findViewById<TextView>(R.id.prdctpricerow)
    val productImage:  ImageView = itemView.findViewById(R.id.prdctimgnewsrow)
    val deleteRow: ImageView = itemView.findViewById(R.id.deleterow)





}