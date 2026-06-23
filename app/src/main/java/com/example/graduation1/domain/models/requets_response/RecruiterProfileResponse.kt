package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class RecruiterProfileResponse(
    @SerializedName("recruiterProfileID") val recruiterProfileID: Int,
    @SerializedName("userID")             val userID: Int,
    @SerializedName("companyName")        val companyName: String,
    @SerializedName("companyWebsite")     val companyWebsite: String?,
    @SerializedName("location")           val location: String?,
    @SerializedName("about")              val about: String?,
    @SerializedName("createdAt")          val createdAt: String,
    @SerializedName("updatedAt")          val updatedAt: String?
)