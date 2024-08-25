package com.example.infinity.admin

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.infinity.Utils.Utils
import com.example.infinity.databinding.AdminmodifyproductBinding
import com.example.infinity.model.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso

class adminmodifyproduct : AppCompatActivity() {
    private lateinit var binding: AdminmodifyproductBinding
    private lateinit var productID: String
    private lateinit var PRODUCT: Product
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var ImageUri: Uri? = null  // Make ImageUri nullable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AdminmodifyproductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productID = intent?.extras?.getString("product_id") ?: ""  // Retrieve product ID

        if (productID.isNotEmpty()) {
            retrieveProductData()
        }

        binding.updateproduct.setOnClickListener {
            updateProduct()
        }

        binding.back.setOnClickListener {
            startActivity(Intent(this@adminmodifyproduct, admin::class.java))
            finish()
        }

        binding.selectimg.setOnClickListener {
            openGallery()
        }

        binding.deleteproduct.setOnClickListener {
            deleteProduct()
        }

        setupTypeSelectionDialog()

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                ImageUri = result.data!!.data
                binding.selectimg.setImageURI(ImageUri)
            }
        }
    }

    private fun retrieveProductData() {
        Utils.productref.child(productID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                PRODUCT = snapshot.getValue<Product>() ?: return
                fillData()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@adminmodifyproduct, "Failed to load product data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fillData() {
        binding.prdctname.setText(PRODUCT.name)
        binding.description.setText(PRODUCT.description)
        binding.prdctprice.setText(PRODUCT.price)
        binding.type.setText(PRODUCT.type)
        PRODUCT.prdctimg?.let { Picasso.get().load(it).into(binding.selectimg) }
    }

    private fun updateProduct() {
        binding.updateproduct.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE

        PRODUCT.name = binding.prdctname.text.toString()
        PRODUCT.description = binding.description.text.toString()
        PRODUCT.price = binding.prdctprice.text.toString()
        PRODUCT.type = binding.type.text.toString()

        Utils.productref.child(productID).setValue(PRODUCT).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                ImageUri?.let {
                    uploadPictureToStorage(productID)
                } ?: run {
                    Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show()
                    binding.updateproduct.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }
            } else {
                Toast.makeText(this, "Error updating product, please try again", Toast.LENGTH_SHORT).show()
                binding.updateproduct.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun uploadPictureToStorage(productId: String) {
        val storageRef = Firebase.storage.getReference("Product images").child(productId)

        ImageUri?.let { uri ->
            storageRef.putFile(uri).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storageRef.downloadUrl.addOnCompleteListener { urlTask ->
                        if (urlTask.isSuccessful) {
                            val imageURL = urlTask.result.toString()
                            Utils.productref.child(productId).child("prdctimg").setValue(imageURL).addOnCompleteListener { imageTask ->
                                if (imageTask.isSuccessful) {
                                    Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this, "Error saving image URL, please try again", Toast.LENGTH_SHORT).show()
                                }
                                binding.updateproduct.visibility = View.VISIBLE
                                binding.progressBar.visibility = View.GONE
                            }
                        } else {
                            Toast.makeText(this, "Failed to retrieve image URL", Toast.LENGTH_SHORT).show()
                            binding.updateproduct.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                } else {
                    Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                    binding.updateproduct.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        resultLauncher.launch(intent)
    }

    private fun setupTypeSelectionDialog() {
        val brands = arrayOf("Poster", "Clothing", "Figurine", "Event", "Outils", "Other")
        binding.type.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Select Type")
                .setSingleChoiceItems(brands, -1) { dialogInterface, position ->
                    binding.type.text = brands[position]
                    dialogInterface.dismiss()
                }
                .show()
        }
    }

    private fun deleteProduct() {
        AlertDialog.Builder(this)
            .setTitle("Delete Confirmation")
            .setMessage("Do you really want to delete ${PRODUCT.name}?")
            .setPositiveButton("Yes") { dialogInterface, _ ->
                Utils.productref.child(PRODUCT.id).removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, admin::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Error deleting product, please try again", Toast.LENGTH_SHORT).show()
                    }
                }
                dialogInterface.dismiss()
            }
            .setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .show()
    }
}
