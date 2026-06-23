package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class ChatSendResponse(
    @SerializedName("sessionId") val sessionId: Int,
    @SerializedName("reply")     val reply: String?,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("error")     val error: String?
)