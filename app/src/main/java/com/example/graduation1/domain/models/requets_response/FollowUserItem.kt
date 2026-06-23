package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class FollowUserItem(
    @SerializedName("userID")       val userID: Int,
    @SerializedName("firstName")    val firstName: String,
    @SerializedName("lastName")     val lastName: String,
    @SerializedName("userName")     val userName: String?,
    @SerializedName("profileImage") val profileImage: String?,
    @SerializedName("createdAt")    val createdAt: String
)