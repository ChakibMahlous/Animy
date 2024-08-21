package com.example.infinity.user

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.infinity.R
import com.example.infinity.Utils.Utils
import com.example.infinity.databinding.ActivityConfirmOrderBinding
import com.example.infinity.model.Order
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class ConfirmOrderActivity : AppCompatActivity() {
    lateinit var binding: ActivityConfirmOrderBinding
    lateinit var TOTAL_PRICE: String
    lateinit var wilayas: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Safely get the total price from the intent
        TOTAL_PRICE = intent?.extras?.getString("total_price") ?: ""
        if (TOTAL_PRICE.isEmpty()) {
            // Handle the case where total price is not passed or is empty
            Toast.makeText(this, "Total price is missing", Toast.LENGTH_SHORT).show()
            finish() // End the activity as it cannot proceed without the total price
            return
        }

        binding.totalPrice.text = TOTAL_PRICE

        // Safely access Utils.USER
        Utils.USER?.let { user ->
            binding.nameInput.setText(user.name)
            binding.phoneInput.setText(user.phone)
        } ?: run {
            // Handle the case where USER is null
            Toast.makeText(this, "User information is missing", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        initWilayas()

        binding.back4.setOnClickListener {
            startActivity(Intent(this@ConfirmOrderActivity, home::class.java))
            finish()
        }

        binding.wilaya.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Select your City")
            dialog.setSingleChoiceItems(wilayas, -1) { dialogInterface, i ->
                binding.wilaya.text = wilayas[i]
                dialogInterface.dismiss()
            }
            dialog.show()
        }

        binding.shippingType.setOnClickListener {
            val shippingTypes = arrayOf("Stop desk", "A domicile")
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Select your Shipping Type")
            dialog.setSingleChoiceItems(shippingTypes, -1) { dialogInterface, i ->
                binding.shippingType.text = shippingTypes[i]
                dialogInterface.dismiss()
            }
            dialog.show()
        }

        binding.ConfirmOrder.setOnClickListener {
            confirmOrder()
        }
    }

    private fun initWilayas() {
        wilayas = arrayOf(
            "Adrar", "Chlef", "Laghouat", "Oum el Bouaghi", "Batna", "Bejaia", "Biskra", "Bechar", "Blida", "Bouira",
            "Tamanrasset", "Tbessa", "Tlemcen", "Tiaret", "Tizi ouezzo", "Alger", "Djelfa", "Djijel", "Setif", "Saida",
            "Sidi bel abbes", "Annaba", "Guelma", "Constantine", "Medea", "Mostaganem", "Mssila", "Mascara", "Ouergla"
        )
    }

    private fun confirmOrder() {
        binding.progressbar.visibility = View.VISIBLE
        binding.ConfirmOrder.visibility = View.GONE

        // Check if CartFrag.PRODUCTS is not null or empty
        if (CartFrag.PRODUCTS.isNullOrEmpty()) {
            Toast.makeText(this, "No products in the cart", Toast.LENGTH_SHORT).show()
            binding.progressbar.visibility = View.GONE
            binding.ConfirmOrder.visibility = View.VISIBLE
            return
        }

        val orderRef = Utils.orderRef.push()
        val orderID = orderRef.key ?: ""

        val order = Order(
            binding.nameInput.text.toString(),
            binding.phoneInput.text.toString(),
            binding.wilaya.text.toString(),
            binding.commune.text.toString(),
            binding.Adress.text.toString(),
            binding.shippingType.text.toString(),
            binding.comment.text.toString(),
            Firebase.auth.currentUser!!.uid,
            CartFrag.PRODUCTS,
            binding.totalPrice.text.toString(),
            "waiting",
            orderID
        )

        orderRef.setValue(order).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Utils.cartref.removeValue().addOnCompleteListener { task ->
                    binding.progressbar.visibility = View.GONE
                    binding.ConfirmOrder.visibility = View.VISIBLE

                    if (task.isSuccessful) {
                        val dialog = AlertDialog.Builder(this)
                        dialog.setTitle("Order received!")
                        dialog.setMessage("Thank you for your order, we will get back to you soon.")
                        dialog.setPositiveButton("OK") { _, _ ->
                            startActivity(Intent(this@ConfirmOrderActivity, home::class.java))
                            finish()
                        }
                        dialog.show()
                    } else {
                        Toast.makeText(this, "Failed to clear cart: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                binding.progressbar.visibility = View.GONE
                Toast.makeText(this, "Order failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
