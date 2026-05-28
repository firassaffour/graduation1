package com.example.graduation1.domain.models

data class User (
    val id : String = "",
    val name : String = "",
    val image : Any = "",
    val email : String = "",
    val description : String = "",
    val location : String = "",
    val postsList : List<String> = emptyList(),
    val posts : String = postsList.size.toString(),
    val followersList : List<String> = emptyList(),
    val followers : String = followersList.size.toString(),
    val followingList : List<String> = emptyList(),
    val following : String = followingList.size.toString(),
    var groupsList : List<String> = emptyList(),
    val gender : String = "",
    val birthday : String = "",
    var isOnline : Boolean = false,
)