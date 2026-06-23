package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("messageID")  val messageID: Int = 0,
    @SerializedName("content")    val content: String? = null,
    @SerializedName("sentAt")     val sentAt: String? = null,
    @SerializedName("isSeen")     val isSeen: Boolean = false,
    @SerializedName("senderID")   val senderID: Int? = null,
    @SerializedName("receiverID") val receiverID: Int? = null,
    @SerializedName("sender")     val sender: RegisterResponse? = null,
    @SerializedName("receiver")   val receiver: RegisterResponse? = null
)