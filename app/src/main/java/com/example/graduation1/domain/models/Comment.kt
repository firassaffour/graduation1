package com.example.graduation1.domain.models

data class Comment (
    val commentId : String = "",
    val userId : String = "",
    val text : String = "",
    val date : String = "",
    var isLiked : Boolean = false,
    val likesCount : Int = 0
)