package com.example.graduation1.domain.models

data class PostResponse(
    val postID: Int,
    val title: String?,
    val content: String?,
    val codeSnippet: String?,
    val createdAt: String?,
    val userID: Int?,
    val communityID: Int?,
    val user: RegisterResponse?,
    val media: MediaResponse?
)