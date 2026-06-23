package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class LikeStatusResponse(
    @SerializedName("hasLiked") val hasLiked: Boolean
)