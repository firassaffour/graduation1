package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class CommunityMemberItem(
    @SerializedName("userID")    val userID: Int,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName")  val lastName: String,
    @SerializedName("joinedAt")  val joinedAt: String
)