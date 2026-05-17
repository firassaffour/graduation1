package com.example.graduation1.data.repository

import com.example.graduation1.data.remote.ApiService
import com.example.graduation1.domain.models.PostData

class PostRepository(private val api: ApiService) {

    suspend fun getPosts() = api.getPosts()

    suspend fun createPost(post : PostData) = api.createPost(post)

    suspend fun updateLike(postId : String, post: PostData) = api.updateLike(postId, post)
}