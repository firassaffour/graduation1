package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class UnreadCountResponse(
    @SerializedName("unreadCount") val unreadCount: Int
)