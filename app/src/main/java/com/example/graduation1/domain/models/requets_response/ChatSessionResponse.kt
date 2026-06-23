package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class ChatSessionResponse(
    @SerializedName("sessionID")  val sessionID: Int,
    @SerializedName("title")      val title: String?,
    @SerializedName("createdAt")  val createdAt: String
)