package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class CodeSubmissionResponse(
    @SerializedName("submissionID") val submissionID: Int,
    @SerializedName("userID")       val userID: Int?,
    @SerializedName("postID")       val postID: Int?,
    @SerializedName("code")         val code: String?,
    @SerializedName("language")     val language: String?,
    @SerializedName("createdAt")    val createdAt: String,
    @SerializedName("aIResponses")  val aiResponses: List<AiResponseItem>?
)