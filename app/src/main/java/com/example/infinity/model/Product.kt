package com.example.infinity.model

data class Product(
    var name : String,
    var description : String,
    var price : String,
    var type : String,
    val id : String,
    var prdctimg : String?,
){
    constructor() : this("","","","","","")
}
