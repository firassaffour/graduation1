package com.example.graduation1.domain.models

data class PostData(
    val postId : String = "",
    val groupId : String = "",
    val userId : String = "",
    val groupName : String = "",
    val groupImage : String = "",
    val publisherName: String = "",
    val publisherPhoto : String = "",
    val postText : String = "",
    val postImage : Any = "",
    val codeSnippet : String = "",
    val postDate : String = "",
    var isLiked : Boolean = false,
    var isSaved : Boolean = false,
    var likesCount : Int = 0,
    val commentsList : List<Comment> = emptyList(),
    var commentsCount : Int = commentsList.size

)