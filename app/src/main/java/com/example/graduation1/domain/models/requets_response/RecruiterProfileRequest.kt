package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class RecruiterProfileRequest(
    @SerializedName("companyName")    val companyName: String,
    @SerializedName("companyWebsite") val companyWebsite: String? = null,
    @SerializedName("location")       val location: String? = null,
    @SerializedName("about")          val about: String? = null
)