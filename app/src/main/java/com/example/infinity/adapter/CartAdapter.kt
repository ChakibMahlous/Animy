package com.example.infinity.adapter

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.infinity.R
import com.example.infinity.Utils.Utils
import com.example.infinity.model.Product
import com.example.infinity.user.CartFrag
import com.example.infinity.viewholder.prodectviewholder
import com.squareup.picasso.Picasso

class CartAdapter (
    val context: Context,
    val data : ArrayList<Product>
):RecyclerView.Adapter<prodectviewholder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): prodectviewholder {
        return prodectviewholder(
            LayoutInflater.from(context).inflate(R.layout.row_product,parent,false)
        )
    }

    override fun getItemCount(): Int {
         return data.size
    }

    override fun onBindViewHolder(holder: prodectviewholder, position: Int) {
       holder.productName.text = data[position].name
       holder.productPrice.text = "${data[position].price} DZD"
       Picasso.get().load(data[position].prdctimg).into(holder.productImage)
        holder.deleteRow.setOnClickListener {
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("Delete Confirmation")
            dialog.setMessage("Do you really want to delete ${data[position].name} ?")
            dialog.setPositiveButton("Oui", DialogInterface.OnClickListener{
                    dialogInterface, i ->
                Utils.cartref.child(data[position].id).removeValue()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            CartFrag.PRODUCTS.removeAt(position)
                            CartFrag.binding.prdctrecyclerviewpannier.adapter!!.notifyDataSetChanged()
                            CartFrag.calctotalprice()
                            if (CartFrag.PRODUCTS.isEmpty()){
                                CartFrag.binding.cartempty.visibility = View.VISIBLE
                            }
                            Toast.makeText(context,"Product deleted !",Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(context,task.exception!!.message,Toast.LENGTH_SHORT).show()
                        }
                    }


            })
            dialog.setNegativeButton("Non", DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
            })
            dialog.show()



        }
    }


}