package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class CommentResponse(
    @SerializedName("commentID") val commentID: Int = 0,
    @SerializedName("content")   val content: String? = null,
    @SerializedName("sentAt")    val sentAt: String? = null,
    @SerializedName("userID")    val userID: Int? = null,
    @SerializedName("postID")    val postID: Int? = null,
    @SerializedName("user")      val user: RegisterResponse? = null
)