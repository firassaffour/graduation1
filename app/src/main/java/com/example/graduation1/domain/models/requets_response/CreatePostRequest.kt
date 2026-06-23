package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class CreatePostRequest(
    @SerializedName("title")       val title: String? = null,
    @SerializedName("content")     val content: String,
    @SerializedName("codeSnippet") val codeSnippet: String? = null,
    @SerializedName("communityID") val communityID: Int? = null
)