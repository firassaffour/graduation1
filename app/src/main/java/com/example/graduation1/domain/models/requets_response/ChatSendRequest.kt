package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class ChatSendRequest(
    @SerializedName("sessionId") val sessionId: Int,
    @SerializedName("message")   val message: String
)