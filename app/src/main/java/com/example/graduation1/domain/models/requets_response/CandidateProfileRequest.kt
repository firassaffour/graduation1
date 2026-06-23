package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class CandidateProfileRequest(
    @SerializedName("country")            val country: String? = null,
    @SerializedName("governorate")        val governorate: String? = null,
    @SerializedName("yearsOfExperience")  val yearsOfExperience: Int = 0,
    @SerializedName("preferredRole")      val preferredRole: String? = null,
    @SerializedName("bio")                val bio: String? = null,
    @SerializedName("availabilityStatus") val availabilityStatus: String? = null
)