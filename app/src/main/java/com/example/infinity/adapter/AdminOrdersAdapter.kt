package com.example.infinity.adapter

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.infinity.R
import com.example.infinity.Utils.Utils
import com.example.infinity.model.Order
import com.example.infinity.viewholder.OrderViewHolder

class AdminOrdersAdapter (
    val context: Context,
    val data : ArrayList<Order>
): RecyclerView.Adapter<OrderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(
            LayoutInflater.from(context).inflate(R.layout.row_orders,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.orderName.text = data[position].name
        holder.orderPhone.text = data[position].PhoneNumber
        holder.orderPrice.text = data[position].totalprice
        holder.orderAddress.text = data[position].address
        holder.orderShippingType.text = data[position].typeShipping
        holder.orderState.text = data[position].state
        holder.order_products.text = data[position].products[0].name
        when (data[position].state) {
            "waiting" -> {
                holder.orderState.setBackgroundResource(R.drawable.backgroundenattente)
                holder.cancelOrder.visibility = View.VISIBLE
                holder.receive_order.text = "Ship Order"

                holder.receive_order.visibility = View.VISIBLE
            }
            "cancelled" -> {
                holder.orderState.setBackgroundResource(R.drawable.backgroundannuler)
                holder.cancelOrder.visibility = View.GONE
                holder.receive_order.visibility = View.GONE

            }
            "shipped" -> {
                holder.orderState.setBackgroundResource(R.drawable.backgroundenlivraison)
                holder.cancelOrder.visibility = View.GONE
                holder.receive_order.visibility = View.GONE


            }
            "done" -> {
                holder.orderState.setBackgroundResource(R.drawable.backgrounddone)
                holder.cancelOrder.visibility = View.GONE
                holder.receive_order.visibility = View.GONE


            }
        }
        when (data[position].typeShipping){
            "Stop desk" -> {
                holder.orderShippingType.setBackgroundResource(R.drawable.backgroundstopdesk)
            }
            "A domicile" -> {
                holder.orderShippingType.setBackgroundResource(R.drawable.backgroundadomicile)

            }
        }
        holder.cancelOrder.setOnClickListener {
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("cancel order")
            dialog.setMessage("Do You really want to cancel your order ?")
            dialog.setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
                Utils.orderRef.child(data[position].orderID)
                    .child("state").setValue("cancelled")
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            holder.orderState.text = "cancelled"
                            holder.orderState.setBackgroundResource(R.drawable.backgroundannuler)
                            holder.cancelOrder.visibility = View.GONE


                        }

                    }
            })
            dialog.setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
            })
            dialog.show()
        }
        holder.receive_order.setOnClickListener {
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("Ship order")
            dialog.setMessage("Did You really Ship this order ?")
            dialog.setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
                Utils.orderRef.child(data[position].orderID)
                    .child("state").setValue("shipped")
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            holder.orderState.text = "shipped"
                            holder.orderState.setBackgroundResource(R.drawable.backgroundenlivraison)
                            holder.receive_order.visibility = View.GONE


                        }

                    }
            })
            dialog.setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
            })
            dialog.show()
        }

    }
}