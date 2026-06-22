package com.example.graduation1.data.repository

import android.util.Log
import com.example.graduation1.data.remote.ApiService
import com.example.graduation1.domain.models.User

class UserRepository(private val api: ApiService) {

    suspend fun getUsers() : List<User>{
        val response = api.getUsers()
        Log.e("API_USERS", "getUsers: $response", )
        return api.getUsers().map {
            User(
                id = it.userID.toString(),
                name = it.firstName,
                email = it.email,
                image = it.profileImage ?: "",
                description = it.bio ?: "New Developer",
                gender = "Male",
                location = "Egypt",
                groupsList = emptyList()
            )
        }
    }

    private var currentUser: User? = null

    suspend fun getCurrentUser(): User {
        if (currentUser == null) {
            val response = api.getCurrentUser()
            currentUser = User(
                id = response.userID.toString(),
                name = response.firstName,
                email = response.email,
                image = response.profileImage ?: "",
                description = response.bio ?: "New Developer",
                gender = "Male",
                location = "Egypt",
                groupsList = emptyList()
            )
        }
        return currentUser!!
    }

    suspend fun getUserDetails(id : String) = api.getUserDetails(id)

    suspend fun editUser(id : String, user : User) = api.editUser(id, user)

    suspend fun deleteUser(id : String) = api.deleteUser(id)

    suspend fun followUser(id: String) = api.followUser(id)

    suspend fun  unfollowUser(id: String) = api.unfollowUser(id)

    suspend fun getFollowers(id: String) = api.getFollowers(id)

    suspend fun getFollowing(id: String) = api.getFollowing(id)

    suspend fun getFollowingStatus(id: String) = api.getFollowStatus(id)

}