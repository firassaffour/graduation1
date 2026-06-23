package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class AiResponseItem(
    @SerializedName("responseID")   val responseID: Int,
    @SerializedName("submissionID") val submissionID: Int?,
    @SerializedName("content")      val content: String?,
    @SerializedName("createdAt")    val createdAt: String
)