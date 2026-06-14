package com.example.graduation1.domain.models

data class PostData(
    val postId : String = "",
    val groupId : String = "",
    val userId : String = "",
    val postText : String = "",
    val postImage : Any = "",
    val codeSnippet : String = "",
    val createdAt : Long = 0,
    val likesCount : List<String> = emptyList(),
    var isSaved : Boolean = false,
    val commentsList : List<Comment> = emptyList()
)