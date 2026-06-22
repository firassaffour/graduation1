package com.example.graduation1.data.repository

import com.example.graduation1.data.remote.ApiService
import com.example.graduation1.domain.models.PostData
import com.example.graduation1.domain.models.PostResponse

class PostRepository(private val api: ApiService) {

    suspend fun getPosts() : List<PostData>{
        val response = api.getPosts()
        return api.getPosts().map {
            PostData(
                postId = it.postID.toString(),
                postText = it.content ?: "",
                codeSnippet = it.codeSnippet ?: "",
                createdAt = it.createdAt?.toLong() ?: 0,
                userId = it.userID.toString(),
                groupId = it.communityID.toString(),
                postImage = it.media?.filePath.toString()
            )
        }
    }

    suspend fun createPost(post : PostResponse) = api.createPost(post)

    suspend fun updateLike(postId : String, post: PostData) = api.updateLike(postId, post)
}