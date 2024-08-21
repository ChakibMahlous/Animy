package com.example.infinity.model

data class User(
    val name : String,
    val phone: String,
    val email: String,
    var prfilepic : String?,
    val admin : Boolean?
){
    constructor(): this("","","","",null)
}
