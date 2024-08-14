package com.example.infinity.model

data class Order(
    val name : String,
    val PhoneNumber : String,
    val wilaya : String,
    val commune : String,
    val address : String,
    val typeShipping : String,
    val comment : String,
    val userId : String,
    val products : ArrayList<Product>,
    val totalprice : String,
    val state : String,
    val orderID : String,

){
    constructor(): this("","","","","","","",
    "",ArrayList(),"","","")
}
