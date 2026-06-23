package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class PostResponse(
    @SerializedName("postID")      val postID: Int = 0,
    @SerializedName("title")       val title: String? = null,
    @SerializedName("content")     val content: String? = null,
    @SerializedName("codeSnippet") val codeSnippet: String? = null,
    @SerializedName("createdAt")   val createdAt: String? = null,
    @SerializedName("updatedAt")   val updatedAt: String? = null,
    @SerializedName("userID")      val userID: Int? = null,
    @SerializedName("communityID") val communityID: Int? = null,
    @SerializedName("user")        val user: RegisterResponse? = null,
    @SerializedName("media")       val media: List<MediaResponse>? = null,
    @SerializedName("likes")       val likes: List<Any>? = null,
    @SerializedName("comments")    val comments: List<Any>? = null
)