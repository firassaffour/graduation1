package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class NewSessionRequest(
    @SerializedName("title") val title: String? = null
)