package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class ChatMessageResponse(
    @SerializedName("messageID")  val messageID: Int,
    @SerializedName("sessionID")  val sessionID: Int,
    @SerializedName("role")       val role: String,   // "user" or "assistant"
    @SerializedName("content")    val content: String?,
    @SerializedName("createdAt")  val createdAt: String
)