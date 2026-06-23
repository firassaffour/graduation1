package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class CandidateRankingItem(
    @SerializedName("userID")             val userID: Int,
    @SerializedName("displayName")        val displayName: String,
    @SerializedName("preferredRole")      val preferredRole: String?,
    @SerializedName("country")            val country: String?,
    @SerializedName("availabilityStatus") val availabilityStatus: String?,
    @SerializedName("overallScore")       val overallScore: Int,
    @SerializedName("matchPercentage")    val matchPercentage: Int,
    @SerializedName("strongSkills")       val strongSkills: List<String>,
    @SerializedName("missingSkills")      val missingSkills: List<String>
)