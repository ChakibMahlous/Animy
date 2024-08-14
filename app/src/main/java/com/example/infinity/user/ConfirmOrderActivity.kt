package com.example.infinity.user

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
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
    lateinit var TOTAL_PRICE : String
    lateinit var wilayas :Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmOrderBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        TOTAL_PRICE = intent!!.extras!!.getString("total_price")!!
        binding.totalPrice.text = TOTAL_PRICE
        binding.nameInput.setText(Utils.USER!!.name)
        binding.phoneInput.setText(Utils.USER!!.phone)

        initWilayas()
        binding.back4.setOnClickListener{
            startActivity(Intent(this@ConfirmOrderActivity,home::class.java))
            finish()
        }

        binding.wilaya.setOnClickListener{
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Select your City")
            dialog.setSingleChoiceItems(wilayas,-1,DialogInterface.OnClickListener { dialogInterface, i ->
                binding.wilaya.text = wilayas[i]
                dialogInterface.dismiss()
            })
            dialog.show()
        }
        binding.shippingType.setOnClickListener {
            val shippingTypes = arrayOf("Stop desk","A domicile")
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Select your City")
            dialog.setSingleChoiceItems(shippingTypes,-1,DialogInterface.OnClickListener { dialogInterface, i ->
                binding.shippingType.text = shippingTypes[i]
                dialogInterface.dismiss()
            })
            dialog.show()
        }
        binding.ConfirmOrder.setOnClickListener {
            confirmOrder()
        }




    }
    fun initWilayas(){
         wilayas = arrayOf(
            "Adrar","Chlef","Laghouat","Oum el Bouaghi","Batna","Bejaia","Biskra","Bechar","Blida","Bouira",
            "Tamanrasset","Tbessa","Tlemcen","Tiaret","Tizi ouezzo","Alger","Djelfa","Djijel","Setif","Saida",
            "Sidi bel abbes","Annaba","Guelma","Constantine","Medea","Mostaganem","Mssila","Mascara","Ouergla",
        )
    }
    //@SuppressLint("SuspiciousIndentation")
    fun confirmOrder(){
        binding.progressbar.visibility = View.VISIBLE
        binding.ConfirmOrder.visibility = View.GONE
     val orderRef = Utils.orderRef.push()
     val orderID = orderRef.key
     val order = Order(//hna dir if is nof empty bch matdich comande vide
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
                 orderID!!,
     )
        orderRef.setValue(order).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Utils.cartref.removeValue().addOnCompleteListener { task->
                    if (task.isSuccessful){
                        binding.ConfirmOrder.visibility = View.VISIBLE

                        binding.progressbar.visibility = View.GONE
                        binding.ConfirmOrder.setOnClickListener {
                            val dialog = AlertDialog.Builder(this)
                            dialog.setTitle("Order received !")
                            dialog.setMessage("Thank you for your order, we will back to you soon ")
                            dialog.setPositiveButton("ok",DialogInterface.OnClickListener { dialogInterface, i ->
                                startActivity(Intent(this@ConfirmOrderActivity,home::class.java))
                                finish()
                            })
                            dialog.show()

                        }

                        val dialog = AlertDialog.Builder(this)
                        dialog.setTitle("Order received !")
                        dialog.setMessage("Thank you for your order, we will back to you soon ")
                        dialog.setPositiveButton("ok",DialogInterface.OnClickListener { dialogInterface, i ->
                            startActivity(Intent(this@ConfirmOrderActivity,home::class.java))
                            finish()
                        })
                        dialog.show()
                    }else{
                        binding.progressbar.visibility = View.GONE

                    }

                }
            }else{
                binding.progressbar.visibility = View.GONE


            }

        }
    }

}