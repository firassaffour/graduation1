package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class CreateCommunityRequest(
    @SerializedName("name")        val name: String,
    @SerializedName("description") val description: String? = null
)