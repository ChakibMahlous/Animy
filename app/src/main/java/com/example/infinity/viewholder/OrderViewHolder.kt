package com.example.infinity.viewholder

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.infinity.R

class OrderViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
    val orderName = itemView.findViewById<TextView>(R.id.order_name)
    val orderPhone = itemView.findViewById<TextView>(R.id.order_number)
    val orderState = itemView.findViewById<TextView>(R.id.order_state)
    val orderPrice = itemView.findViewById<TextView>(R.id.order_price)
    val orderAddress = itemView.findViewById<TextView>(R.id.order_adress)
    val orderShippingType = itemView.findViewById<TextView>(R.id.order_shipping_type)
    val cancelOrder = itemView.findViewById<Button>(R.id.cancel_order)
    val order_products = itemView.findViewById<TextView>(R.id.order_product)
    val receive_order = itemView.findViewById<Button>(R.id.confirm_reception)
}