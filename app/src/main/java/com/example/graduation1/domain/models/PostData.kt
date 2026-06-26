package com.example.graduation1.domain.models

data class PostData(
    val postId : String = "",
    val groupId : String = "",
    val userId : String = "",
    val postText : String = "",
    val postImage : String = "",
    val codeSnippet : String = "",
    val createdAt : Long = 0,
    val likesCount : Int = 0,
    val isLiked : Boolean = false,
    var isSaved : Boolean = false,
    val commentsList : List<Comment> = emptyList()
)