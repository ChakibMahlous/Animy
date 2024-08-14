package com.example.infinity.admin

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.infinity.R
import com.example.infinity.Utils.Utils
import com.example.infinity.databinding.AddproductBinding
import com.example.infinity.databinding.AdminBinding
import com.example.infinity.model.Product
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.storage.storage

class addproduct : AppCompatActivity() {
    lateinit var binding: AddproductBinding
    private lateinit var auth: FirebaseAuth
    lateinit var productRef :DatabaseReference
    lateinit var resultlauncher: ActivityResultLauncher<Intent>
    lateinit var ImageUri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)

        super.onCreate(savedInstanceState)
        binding = AddproductBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth
        binding.back.setOnClickListener {
            val i= Intent(this@addproduct,admin::class.java)
            startActivity(i)

        }
        binding.add.setOnClickListener {
            addproducttoDB()
        }
        diag()
        resultlauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
            ImageUri = result.data!!.data!!
            binding.selectimg.setImageURI(ImageUri)

        }
       binding.selectimg.setOnClickListener{
           Utils.opengallery(resultlauncher)
           binding.txtselectimg.visibility = View.GONE
       }








    }
    fun addproducttoDB(){
        binding.add.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        productRef = Firebase.database.getReference("product").push()
        val poductID = productRef.key
        val product = Product(binding.prdctname.text.toString(),
                              binding.description.text.toString(),
                              binding.prdctprice.text.toString(),
                              binding.type.text.toString(),
                              poductID!!,
            null
            )
        productRef.setValue(product).addOnCompleteListener{task ->
            if (task.isSuccessful){
                uploadpicturetostorage(poductID)
                Toast.makeText(this,"data saved",Toast.LENGTH_LONG).show()

            }
        }

    }
    fun diag(){
        val brands = arrayOf("Poster","Clothing","Figurine","Event","outils","other")
        binding.type.setOnClickListener{
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("select type")
            dialog.setSingleChoiceItems(brands,-1,DialogInterface.OnClickListener { dialogInterface, position ->

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
                productRef.child("prdctimg").setValue(imageURL)
                    .addOnCompleteListener { task->
                        if (task.isSuccessful){
                            clearFields()
                            Toast.makeText(this,"Product add successfully",Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(this,"Error try Again",Toast.LENGTH_LONG).show()

                        }
                    }
            }
            binding.add.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }

    }
}


    fun clearFields(){
        binding.prdctname.setText("")
        binding.description.setText("")
        binding.type.setText("")
        binding.prdctprice.setText("")
        binding.selectimg.setImageURI(null)
    }



}