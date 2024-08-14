package com.example.infinity.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.infinity.R
import com.example.infinity.Utils.Utils
import com.example.infinity.model.Product
import com.example.infinity.user.ProductDetailsActivity
import com.example.infinity.viewholder.UserProductViewHolder
import com.squareup.picasso.Picasso

class UserProductAdapter(val context: Context, val data : ArrayList<Product>):RecyclerView.Adapter<UserProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserProductViewHolder {
        return UserProductViewHolder(
            LayoutInflater.from(context).inflate(R.layout.row_userprdct,parent,false)
        )

    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: UserProductViewHolder, position: Int) {
        holder.productName.text = data[position].name
        holder.productPrice.text = " ${data[position].price} DZD"
        holder.producttype.text = data[position].type
        Picasso.get().load(data[position].prdctimg).into(holder.productImage)
        holder.addtocart.setOnClickListener{
            Utils.cartref.child(data[position].id).setValue(data[position])
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        Toast.makeText(context,"${data[position].name} added to cart",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context,task.exception!!.message,Toast.LENGTH_SHORT).show()

                    }

                }
        }
        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context,ProductDetailsActivity::class.java)
                .putExtra("product_id",data[position].id))
        }
    }



}