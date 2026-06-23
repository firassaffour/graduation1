package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class CandidateProfileResponse(
    @SerializedName("candidateProfileID") val candidateProfileID: Int,
    @SerializedName("userID")             val userID: Int,
    @SerializedName("country")            val country: String?,
    @SerializedName("governorate")        val governorate: String?,
    @SerializedName("yearsOfExperience")  val yearsOfExperience: Int,
    @SerializedName("preferredRole")      val preferredRole: String?,
    @SerializedName("bio")                val bio: String?,
    @SerializedName("availabilityStatus") val availabilityStatus: String?,
    @SerializedName("createdAt")          val createdAt: String,
    @SerializedName("updatedAt")          val updatedAt: String?
)