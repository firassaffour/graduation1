package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class ExperienceResponse(
    @SerializedName("experienceDescription") val experienceDescription: String?,
    @SerializedName("roleUnderstanding")     val roleUnderstanding: String?,
    @SerializedName("cvBullets")             val cvBullets: List<String>
)