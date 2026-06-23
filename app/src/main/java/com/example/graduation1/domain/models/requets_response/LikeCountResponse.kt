package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class LikeCountResponse(
    @SerializedName("postID")     val postID: Int,
    @SerializedName("likesCount") val likesCount: Int
)