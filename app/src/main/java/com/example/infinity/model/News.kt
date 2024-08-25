package com.example.infinity.model

data class News(
    var title : String,
    var description : String,
    val id : String,
    var imagenews : String? = null
){
    constructor(): this("","","","")
}
