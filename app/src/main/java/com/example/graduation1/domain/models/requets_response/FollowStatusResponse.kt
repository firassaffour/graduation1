package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class FollowStatusResponse(
    @SerializedName("isFollowing") val isFollowing: Boolean
)