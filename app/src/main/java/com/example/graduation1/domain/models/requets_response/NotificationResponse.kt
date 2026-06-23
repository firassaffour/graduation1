package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("notificationID") val notificationID: Int,
    @SerializedName("userID")         val userID: Int,
    @SerializedName("type")           val type: String?,
    @SerializedName("message")        val message: String?,
    @SerializedName("isRead")         val isRead: Boolean,
    @SerializedName("createdAt")      val createdAt: String
)