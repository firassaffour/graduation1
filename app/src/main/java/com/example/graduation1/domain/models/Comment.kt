package com.example.graduation1.domain.models

data class Comment (
    val commentId : String = "",
    val userId : String = "",
    val text : String = "",
    val createdAt : Long = 0,
    var isLiked : Boolean = false,
    val likesCount : Int = 0
)