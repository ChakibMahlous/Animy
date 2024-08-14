package com.example.infinity.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infinity.R
import com.example.infinity.Utils.Utils
import com.example.infinity.adapter.UserProductAdapter
import com.example.infinity.databinding.FragmentHomeBinding
import com.example.infinity.model.Product
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.squareup.picasso.Picasso


class HomeFrag : Fragment() {

    lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        val view = binding.root
        Utils.productref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = ArrayList<Product>()
                val productchildren = snapshot.children
                productchildren.forEach { productChild ->
                    data.add(productChild.getValue<Product>()!!)


                }
                val adapter = UserProductAdapter(requireContext(),data)
                binding.productrecyclerview.adapter = adapter
                val layoutManager = LinearLayoutManager(requireContext())
                binding.productrecyclerview.layoutManager = layoutManager
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        binding.hellouser.text = "Welcome ${Utils.USER!!.name}"
        if (Utils.USER!!.prfilepic!= null){
            if  (Utils.USER!!.prfilepic!!.isNotEmpty()){
                Picasso.get().load(Utils.USER!!.prfilepic!!).into(binding.user)
            }
        }







        return view
    }
    //fun liredata(){
       // val database = Firebase.database
      //  val UserID = Firebase.auth.currentUser!!.uid
       // val myRef = database.getReference("Users").child(UserID).child("name")
       // myRef.addValueEventListener(object : ValueEventListener {
           // override fun onDataChange(dataSnapshot: DataSnapshot) {

               // val username = dataSnapshot.getValue<String>()
               // binding.hellouser.text = "Hello ${username}"
            //!!.split("")[0]}"//hdi bch t9ra 1er element brk
            }

           // override fun onCancelled(error: DatabaseError) {

          //  }
       // })
   // }

//}