package com.example.graduation1.domain.models

import com.example.graduation1.domain.models.requets_response.FollowUserItem

data class User (
    val id : String = "",
    val name : String = "",
    val userName : String = "",
    val image : Any = "",
    val email : String = "",
    val phone : String = "",
    val description : String = "",
    val location : String = "",
    val postsList : List<String> = emptyList(),
    val followersList : List<FollowUserItem> = emptyList(),
    val followingList : List<FollowUserItem> = emptyList(),
    val groupsList : List<String> = emptyList(),
    val gender : String = "",
    val birthday : String = "",
    var isOnline : Boolean = false,
    val isFollowedByMe : Boolean = false
)