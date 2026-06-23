package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class UpdateUserRequest (
    @SerializedName("firstName")    val firstName: String? = null,
    @SerializedName("lastName")     val lastName: String? = null,
    @SerializedName("bio")          val bio: String? = null,
    @SerializedName("profileImage") val profileImage: String? = null
)