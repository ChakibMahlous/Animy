package com.example.infinity.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infinity.R
import com.example.infinity.Utils.Utils
import com.example.infinity.adapter.AdminOrdersAdapter
import com.example.infinity.databinding.FragmentAdminOrdersBinding
import com.example.infinity.model.Order
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue


class AdminOrdersFrag : Fragment() {
    lateinit var binding: FragmentAdminOrdersBinding
    lateinit var STATES : ArrayList<TextView>
    var CURRENT_STATE = 0
    lateinit var ORDERS : ArrayList<Order>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdminOrdersBinding.inflate(layoutInflater)
        val view = binding.root
        STATES = ArrayList()
        ORDERS = ArrayList()
        STATES.add(binding.buttonwaiting)
        STATES.add(binding.buttonShipped)
        STATES.add(binding.buttonFinished)
        retrieveOrders(0)

        binding.buttonwaiting.setOnClickListener {
            changeLayout(0)
            retrieveOrders(0)
        }
        binding.buttonShipped.setOnClickListener {
            changeLayout(1)
            retrieveOrders(1)
        }
        binding.buttonFinished.setOnClickListener {
            changeLayout(2)
            retrieveOrders(2)
        }








        return view
    }
    fun changeLayout(activeElement : Int){
        CURRENT_STATE = activeElement
        STATES.forEach { state ->
            state.setTextColor(requireContext().getColor(R.color.black))
            state.setBackgroundColor(requireContext().getColor(R.color.white))
        }
        STATES[activeElement].setTextColor(requireContext().getColor(R.color.white))
        STATES[activeElement].setBackgroundResource(R.drawable.solid_purple)

    }
    fun retrieveOrders(activeElement: Int){
        Utils.orderRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
              ORDERS.clear()
              val ordersChildren = snapshot.children
              ordersChildren.forEach { orderSnapShot ->
                  val order = orderSnapShot.getValue<Order>()
                  if (activeElement == 0){
                      if (order!!.state == "waiting"){
                          ORDERS.add(order)
                      }
                  }else if (activeElement == 1){
                      if(order!!.state == "shipped"){
                          ORDERS.add(order)
                      }
                  }else if (activeElement == 2){
                      if (order!!.state == "cancelled"){
                          ORDERS.add(order)

                      }else if (order!!.state == "done"){
                          ORDERS.add(order)

                      }
                  }

              }
              val layoutManager = LinearLayoutManager(requireContext())
              layoutManager.reverseLayout = true
              layoutManager.stackFromEnd = true
              binding.orderAdmin.layoutManager = layoutManager
              binding.orderAdmin.adapter = AdminOrdersAdapter(requireContext(),ORDERS)
              if (CURRENT_STATE == 0){
                  binding.buttonwaiting.text = "waiting (${ORDERS.size})"
              }else if (CURRENT_STATE == 1){
                  binding.buttonShipped.text = "shipped (${ORDERS.size})"

              }else if(CURRENT_STATE == 2){
                  binding.buttonFinished.text = "finished (${ORDERS.size})"

              }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}