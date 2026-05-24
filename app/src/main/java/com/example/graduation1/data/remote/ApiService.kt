package com.example.graduation1.data.remote

import com.example.graduation1.domain.models.Notification
import com.example.graduation1.domain.models.PostData
import com.example.graduation1.domain.models.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("users")
    suspend fun getUsers() : List<User>

    @GET("users/{commentId}")
    suspend fun getUserDetails(
        @Path("id") id : String) : User

    @PUT("users/{commentId}")
    suspend fun editUser(
        @Path("id") id : String,
        @Body user : User) : User

    @GET("posts")
    suspend fun getPosts() : List<PostData>

    @POST("posts")
    suspend fun createPost(
        @Body post : PostData
    ) : PostData

    @PUT("posts/{commentId}")
    suspend fun updateLike(
        @Path("id") id : String,
        @Body post: PostData) : PostData

    @GET("notification")
    suspend fun getNotification() : List<Notification>
}