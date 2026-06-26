package com.example.graduation1.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.graduation1.data.remote.ApiService
import com.example.graduation1.domain.models.Comment
import com.example.graduation1.domain.models.PostData
import com.example.graduation1.domain.models.requets_response.CommentResponse
import com.example.graduation1.domain.models.requets_response.CreateCommentRequest
import com.example.graduation1.domain.models.requets_response.CreatePostRequest
import com.example.graduation1.domain.models.requets_response.PostResponse
import com.example.graduation1.domain.models.requets_response.ToggleLikeResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.time.LocalDateTime
import java.time.ZoneId

class PostRepository(private val api: ApiService) {

    private val BASE_URL = "https://graduation-project-backend-production-bc68.up.railway.app"

    private fun imageUrl(filePath: String?): String {
        if (filePath.isNullOrBlank()) return ""
        return if (filePath.startsWith("http")) filePath else "$BASE_URL$filePath"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getPosts() : List<PostData>{
        val response = api.getPosts()
        return api.getPosts().map {
            val time = LocalDateTime.parse(it.createdAt)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
            PostData(
                postId      = it.postID.toString(),
                groupId     = it.communityID?.toString() ?: "",
                userId      = it.userID?.toString() ?: "",
                postText    = it.content ?: "",
                codeSnippet = it.codeSnippet ?: "",
                createdAt   = time,
                likesCount  = it.likes?.size ?: 0,
                isLiked     = false,
                postImage   = imageUrl(it.media?.firstOrNull()?.filePath) ?: ""
            )
        }
    }

    suspend fun createPost(postRequest: CreatePostRequest) : PostResponse{
        return api.createPost(postRequest)
    }

    suspend fun updatePost(id : Int ,postRequest: CreatePostRequest) = api.updatePost(id, postRequest)

    suspend fun deletePost(id: String) = api.deletePost(id)

    suspend fun toggleLike(postId : Int) : ToggleLikeResponse{
        return api.toggleLike(postId)
    }

    suspend fun getLikeCount(postId : Int) : Int { return  api.getLikeCount(postId).likesCount}

    suspend fun getLikeStatus(postId : Int) : Boolean { return api.getLikeStatus(postId).hasLiked}

    suspend fun createComment(createCommentRequest: CreateCommentRequest) : CommentResponse{
        return api.createComment(createCommentRequest)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getCommentsByPost(postId: Int) : List<Comment>{
        val response = api.getCommentsByPost(postId)
        return api.getCommentsByPost(postId).map {
            val time = LocalDateTime.parse(it.sentAt)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
            Comment(
                commentId = it.commentID.toString(),
                userId = it.userID.toString(),
                text = it.content ?: "",
                createdAt = time ?: 0,
            )
        }
    }

    suspend fun getCommentDetails(id: String) = api.getCommentDetails(id)

    suspend fun deleteComment(id: Int) = api.deleteComment(id)

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getSavedPosts() : List<PostData>{
        val response = api.getSavedPosts()
        return api.getSavedPosts().map {
            val time = LocalDateTime.parse(it.createdAt)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
            PostData(
                postId = it.postID.toString(),
                postText = it.content ?: "",
                codeSnippet = it.codeSnippet ?: "",
                createdAt = time ?: 0,
                userId = it.userID.toString(),
                groupId = it.communityID.toString(),
                postImage = "$BASE_URL${it.media?.firstOrNull()?.filePath}" ?: ""
            )
        }
    }

    suspend fun savePost(postId: Int) = api.savePost(postId)

    suspend fun unsavePost(postId: Int) = api.unsavePost(postId)
}