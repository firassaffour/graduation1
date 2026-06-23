package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class ExperienceRequest(
    @SerializedName("targetRole") val targetRole: String? = null
)