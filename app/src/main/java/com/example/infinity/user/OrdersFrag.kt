package com.example.infinity.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infinity.R
import com.example.infinity.Utils.Utils
import com.example.infinity.adapter.UserOrderAdapter
import com.example.infinity.databinding.FragmentOrdersBinding
import com.example.infinity.model.Order
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue


class OrdersFrag : Fragment() {

    lateinit var binding: FragmentOrdersBinding
    lateinit var ORDERS : ArrayList<Order>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =FragmentOrdersBinding.inflate(layoutInflater)
        val view = binding.root
        ORDERS = ArrayList()
        Utils.orderRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val orderchildren = snapshot.children
                orderchildren.forEach { orderSnapshot ->
                    val order = orderSnapshot.getValue<Order>()
                    if (order!!.userId == Firebase.auth.currentUser!!.uid){
                        ORDERS.add(order)
                    }


                }
                val layoutManager = LinearLayoutManager(requireContext())
                layoutManager.reverseLayout = true
                layoutManager.stackFromEnd = true
                binding.orders.layoutManager = layoutManager
                binding.orders.adapter = UserOrderAdapter(requireContext(),ORDERS)

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
















        return view
    }

}