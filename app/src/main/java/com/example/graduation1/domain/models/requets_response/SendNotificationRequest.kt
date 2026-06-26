package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class SendNotificationRequest(
    @SerializedName("userID")  val userID: Int,
    @SerializedName("groupID") val groupID: Int? = null,
    @SerializedName("type")    val type: String? = null,
    @SerializedName("message") val message: String? = null
)