package com.example.infinity.admin

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.infinity.R
import com.example.infinity.Utils.Utils
import com.example.infinity.databinding.AdminmodifyproductBinding
import com.example.infinity.model.Product
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.storage
import com.squareup.picasso.Picasso

class adminmodifyproduct : AppCompatActivity() {
    lateinit var binding: AdminmodifyproductBinding
    lateinit var productID : String
    lateinit var PRODUCT : Product
    lateinit var resultlauncher: ActivityResultLauncher<Intent>
    lateinit var ImageUri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AdminmodifyproductBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        productID = intent!!.extras!!.getString("product_id")!! //recuperate ID From adapter
        retrieveProductData()
        binding.updateproduct.setOnClickListener {
             updateprdct()
        }
        binding.back.setOnClickListener {
            val i= Intent(this@adminmodifyproduct,admin::class.java)
            startActivity(i)

        }
        diag()

        resultlauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result ->
            ImageUri = result.data!!.data!!
            binding.selectimg.setImageURI(ImageUri)

        }
        binding.selectimg.setOnClickListener {
            opengallery(resultlauncher)
        }
        binding.deleteproduct.setOnClickListener {
            deleteprdct()
        }



    }
    //read data
    fun retrieveProductData(){
        Utils.productref.child(productID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                PRODUCT = snapshot.getValue<Product>()!!
                filldata()
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })
    }
    //change Data TO use it in read data
    fun filldata(){
        binding.prdctname.setText(PRODUCT.name)
        binding.description.setText(PRODUCT.description)
        binding.prdctprice.setText(PRODUCT.price)
        binding.type.setText(PRODUCT.type)
        Picasso.get().load(PRODUCT.prdctimg).into(binding.selectimg)

    }
    fun updateprdct(){
        binding.updateproduct.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        //we are getting the update information from the edit texts
        PRODUCT.name = binding.prdctname.text.toString()
        PRODUCT.description = binding.description.text.toString()
        PRODUCT.price = binding.prdctprice.text.toString()
        PRODUCT.type = binding.type.text.toString()
        // go to prdctref and make the update
        Utils.productref.child(productID).setValue(PRODUCT)
            .addOnCompleteListener {
                task ->
                if (task.isSuccessful){
                         uploadpicturetostorage(productID)
                }else{
                    Toast.makeText(this,"Error try again",Toast.LENGTH_SHORT).show()
                }
            }
    }
    //change img
    fun opengallery(launcher: ActivityResultLauncher<Intent>){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        launcher.launch(intent)
    }
    fun diag(){
        val brands = arrayOf("Poster","Clothing","Figurine","Event","outils","other")
        binding.type.setOnClickListener{
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("select type")
            dialog.setSingleChoiceItems(brands,-1,
                DialogInterface.OnClickListener { dialogInterface, position ->

                binding.type.text= brands[position]
                dialogInterface.dismiss()
            })
            dialog.show()
        }
    }
    fun uploadpicturetostorage(productId: String) {
        val storageRef = Firebase.storage.getReference("Product images").child(productId)
        storageRef.putFile(ImageUri).addOnCompleteListener { task ->

            if (task.isSuccessful){
                Toast.makeText(this,"image upload",Toast.LENGTH_LONG).show()
                task.result.storage.downloadUrl
                storageRef.downloadUrl.addOnCompleteListener { task ->

                    val imageURL = task.result.toString()
                    Firebase.database.getReference("product").child(productId).child("prdctimg").setValue(imageURL)
                    Utils.productref.child(productId).child("prdctimg").setValue(imageURL)
                        .addOnCompleteListener { task->
                            if (task.isSuccessful){
                                Toast.makeText(this,"Product updated successfully",Toast.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(this,"Error try Again",Toast.LENGTH_LONG).show()

                            }
                        }
                }
                binding.updateproduct.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }

        }
    }
    fun deleteprdct (){
        binding.deleteproduct.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE

        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Delete Confirmation")
        dialog.setMessage("Do you really want to delete ${PRODUCT.name} ?")
        dialog.setPositiveButton("Oui",DialogInterface.OnClickListener{
                dialogInterface, i ->
            Utils.productref.child(PRODUCT.id).removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(this,"Product deleted",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,admin::class.java))
                    finish()
                }else{
                    Toast.makeText(this,"Error try again",Toast.LENGTH_SHORT).show()

                }

            }

        })
        dialog.setNegativeButton("Non", DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
        })
        dialog.show()

    }


}