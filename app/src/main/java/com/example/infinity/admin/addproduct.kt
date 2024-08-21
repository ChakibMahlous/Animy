package com.example.infinity.admin

import android.app.Activity
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
    lateinit var productRef: DatabaseReference
    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        binding = AddproductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.back.setOnClickListener {
            startActivity(Intent(this@addproduct, admin::class.java))
        }

        binding.add.setOnClickListener {
            if (imageUri != null) {
                addProductToDB()
            } else {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_LONG).show()
            }
        }

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                imageUri = result.data!!.data
                binding.selectimg.setImageURI(imageUri)
                binding.txtselectimg.visibility = View.GONE
            }
        }

        binding.selectimg.setOnClickListener {
            Utils.opengallery(resultLauncher)
        }

        setupTypeDialog()
    }

    private fun setupTypeDialog() {
        val brands = arrayOf("Poster", "Clothes", "Figurine", "Event", "Outils", "Others")
        binding.type.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Select type")
            dialog.setSingleChoiceItems(brands, -1) { dialogInterface, position ->
                binding.type.text = brands[position]
                dialogInterface.dismiss()
            }
            dialog.show()
        }
    }

    private fun addProductToDB() {
        binding.add.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        productRef = Firebase.database.getReference("product").push()
        val productId = productRef.key

        val product = Product(
            name = binding.prdctname.text.toString(),
            description = binding.description.text.toString(),
            price = binding.prdctprice.text.toString(),
            type = binding.type.text.toString(),
            id = productId!!,
            prdctimg = null,
            category = binding.type.text.toString() // Set the category equal to the type
        )

        productRef.setValue(product).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                uploadPictureToStorage(productId)
            } else {
                Toast.makeText(this, "Failed to save product data", Toast.LENGTH_LONG).show()
                binding.add.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun uploadPictureToStorage(productId: String) {
        if (imageUri == null) {
            Toast.makeText(this, "No image selected", Toast.LENGTH_LONG).show()
            binding.add.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
            return
        }

        val storageRef = Firebase.storage.getReference("Product images").child(productId)
        storageRef.putFile(imageUri!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                storageRef.downloadUrl.addOnCompleteListener { urlTask ->
                    if (urlTask.isSuccessful) {
                        val imageURL = urlTask.result.toString()
                        productRef.child("prdctimg").setValue(imageURL).addOnCompleteListener { imageTask ->
                            if (imageTask.isSuccessful) {
                                clearFields()
                                Toast.makeText(this, "Product added successfully", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(this, "Failed to save image URL", Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Failed to retrieve image URL", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_LONG).show()
            }
            binding.add.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun clearFields() {
        binding.prdctname.setText("")
        binding.description.setText("")
        binding.type.setText("")
        binding.prdctprice.setText("")
        binding.selectimg.setImageURI(null)
        imageUri = null
    }
}