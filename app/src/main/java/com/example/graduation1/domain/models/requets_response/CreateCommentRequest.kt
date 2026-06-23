package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class CreateCommentRequest(
    @SerializedName("postID")  val postID: Int,
    @SerializedName("content") val content: String
)