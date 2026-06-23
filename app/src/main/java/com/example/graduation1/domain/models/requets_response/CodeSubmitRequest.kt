package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class CodeSubmitRequest(
     @SerializedName("code")     val code: String,
     @SerializedName("language") val language: String? = null,
     @SerializedName("postID")   val postID: Int? = null
)