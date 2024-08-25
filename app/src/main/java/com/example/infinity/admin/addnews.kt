package com.example.infinity.admin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.infinity.Utils.Utils
import com.example.infinity.databinding.AddnewsBinding
import com.example.infinity.model.News
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.storage.storage


class addnews : AppCompatActivity() {
    lateinit var binding: AddnewsBinding
    private lateinit var auth: FirebaseAuth
    lateinit var newsRef: DatabaseReference
    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        binding = AddnewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = com.google.firebase.Firebase.auth

        binding.back.setOnClickListener {
            startActivity(Intent(this@addnews, admin::class.java))
        }

        binding.add.setOnClickListener {
            if (imageUri != null) {
                addNewsToDB()
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
    }

    private fun addNewsToDB() {
        binding.add.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        newsRef = com.google.firebase.Firebase.database.getReference("news").push()
        val newsId = newsRef.key

        val news = News(
            title = binding.newsname.text.toString(),
            description = binding.newsdescription.text.toString(),
            id = newsId!!,
            imagenews = null
        )

        newsRef.setValue(news).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                uploadPictureToStorage(newsId)
            } else {
                Toast.makeText(this, "Failed to save news data", Toast.LENGTH_LONG).show()
                binding.add.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun uploadPictureToStorage(newsId: String) {
        if (imageUri == null) {
            Toast.makeText(this, "No image selected", Toast.LENGTH_LONG).show()
            binding.add.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
            return
        }

        val storageRef = com.google.firebase.Firebase.storage.getReference("News images").child(newsId)
        storageRef.putFile(imageUri!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                storageRef.downloadUrl.addOnCompleteListener { urlTask ->
                    if (urlTask.isSuccessful) {
                        val imageURL = urlTask.result.toString()
                        newsRef.child("imagenews").setValue(imageURL).addOnCompleteListener { imageTask ->
                            if (imageTask.isSuccessful) {
                                clearFields()
                                Toast.makeText(this, "News added successfully", Toast.LENGTH_LONG).show()
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
        binding.newsname.setText("")
        binding.newsdescription.setText("")
        binding.selectimg.setImageURI(null)
        imageUri = null
    }
}
