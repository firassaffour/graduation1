package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class JobApplyRequest(
    @SerializedName("jobId")    val jobId: Int,
    @SerializedName("coverNote") val coverNote: String? = null
)