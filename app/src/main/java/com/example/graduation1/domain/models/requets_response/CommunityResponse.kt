package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class CommunityResponse(
    @SerializedName("communityID")  val communityID: Int = 0,
    @SerializedName("name")         val name: String = "",
    @SerializedName("description")  val description: String? = null,
    @SerializedName("imageUrl")     val imageUrl: String = "",
    @SerializedName("createdBy")    val createdBy: Int? = null,
    @SerializedName("createdAt")    val createdAt: String? = null
)