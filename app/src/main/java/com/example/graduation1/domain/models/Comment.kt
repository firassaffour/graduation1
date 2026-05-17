package com.example.graduation1.domain.models

data class Comment (
    val id : String = "",
    val name : String = "",
    val image : Any = "",
    val text : String = "",
    val date : String = "",
    var isLiked : Boolean = false,
    val likesCount : Int = 0
)