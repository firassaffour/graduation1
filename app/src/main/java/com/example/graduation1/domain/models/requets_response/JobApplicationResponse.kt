package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class JobApplicationResponse(
    @SerializedName("applicationID") val applicationID: Int,
    @SerializedName("jobID")         val jobID: Int,
    @SerializedName("userID")        val userID: Int,
    @SerializedName("status")        val status: String,
    @SerializedName("coverNote")     val coverNote: String?,
    @SerializedName("appliedAt")     val appliedAt: String
)