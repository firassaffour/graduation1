package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class JobMatchResponse(
    @SerializedName("matchPercentage") val matchPercentage: Int,
    @SerializedName("strongSkills")    val strongSkills: List<String>,
    @SerializedName("missingSkills")   val missingSkills: List<String>
)