package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class SendMessageRequest(
    @SerializedName("receiverID") val receiverID: Int,
    @SerializedName("content")    val content: String
)