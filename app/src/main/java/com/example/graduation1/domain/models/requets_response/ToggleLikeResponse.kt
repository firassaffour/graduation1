package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class ToggleLikeResponse(
    @SerializedName("isLiked")    val isLiked: Boolean,
    @SerializedName("likesCount") val likesCount: Int,
    @SerializedName("message")    val message: String
)