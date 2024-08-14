package com.example.infinity.user

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.infinity.R
import com.example.infinity.Utils.Utils
import com.example.infinity.databinding.FragmentAcountBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.storage.storage
import com.squareup.picasso.Picasso
import okhttp3.internal.Util


class AcountFrag : Fragment() {
    lateinit var resultlauncher: ActivityResultLauncher<Intent>
    lateinit var ImageUri : Uri

    lateinit var binding: FragmentAcountBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding = FragmentAcountBinding.inflate(layoutInflater)
        val view = binding.root
        binding.fullemail.text = Utils.USER!!.email
        binding.fullphone.text = Utils.USER!!.phone
        binding.fullname.text = Utils.USER!!.name
        if (Utils.USER!!.prfilepic!= null){
          if  (Utils.USER!!.prfilepic!!.isNotEmpty()){
              Picasso.get().load(Utils.USER!!.prfilepic!!).into(binding.compteimg)
          }
        }
        binding.compteimg.setOnClickListener {
            Utils.opengallery(resultlauncher)
        }

        resultlauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result ->
            ImageUri = result.data!!.data!!
            binding.compteimg.setImageURI(ImageUri)
            uploadprofilepic()

        }

        binding.Logout.setOnClickListener {
            Firebase.auth.signOut()
            Utils.USER = null
            val i= Intent(activity,SignupActivity::class.java)
            startActivity(i)
            activity?.finish()
        }













        return view
    }
    fun uploadprofilepic(){
        binding.progressBar2.visibility =View.VISIBLE
        val storageRef = Firebase.storage.getReference("Profiles pics").child(Firebase.auth.currentUser!!.uid)
        storageRef.child("profilepic.jpeg").putFile(ImageUri)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                  storageRef.child("profilepic.jpeg").downloadUrl
                      .addOnSuccessListener { link->
                          Utils.userRef.child(Firebase.auth.currentUser!!.uid)
                              .child("prfilepic")
                              .setValue(link.toString())
                              .addOnCompleteListener { task ->
                                  if (task.isSuccessful){
                                      binding.progressBar2.visibility =View.GONE

                                      Utils.USER!!.prfilepic = link.toString()
                                      Toast.makeText(requireContext(),"Photo add",Toast.LENGTH_SHORT).show()

                                  }else{
                                      binding.progressBar2.visibility =View.GONE

                                      Toast.makeText(requireContext(),"Error ! try again",Toast.LENGTH_SHORT).show()

                                  }

                              }

                      }
                }else{

                }

            }
    }

}