package com.example.graduation1.data.repository

import android.util.Log
import com.example.graduation1.data.remote.ApiService
import com.example.graduation1.domain.models.User
import com.example.graduation1.domain.models.requets_response.UpdateUserRequest
import com.example.graduation1.emptyProfileImage

class UserRepository(private val api: ApiService) {
    private val BASE_URL = "https://graduation-project-backend-production-bc68.up.railway.app"

    suspend fun getUsers() : List<User>{
        val response = api.getUsers()
        Log.e("API_USERS", "getUsers: $response", )
        return api.getUsers().map {
            User(
                id = it.userID.toString(),
                name = it.firstName,
                userName = it.lastName,
                email = it.email,
                image = "$BASE_URL${it.profileImage}" ?: emptyProfileImage,
                description = it.bio ?: "New Developer",
                gender = "Male",
                location = "Egypt",
                birthday = "01 June 2004",
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
                userName = response.lastName,
                email = response.email,
                image = "$BASE_URL${response.profileImage}" ?: emptyProfileImage,
                description = response.bio ?: "New Developer",
                gender = "Male",
                location = "Egypt",
                birthday = "01 June 2004",
                groupsList = emptyList()
            )
        }
        return currentUser!!
    }

    suspend fun getUserDetails(id : String) = api.getUserDetails(id)

    suspend fun editUser(id : String, updateUserRequest: UpdateUserRequest) {return api.editUser(id, updateUserRequest)}

    suspend fun deleteUser(id : String) { return api.deleteUser(id) }

    suspend fun followUser(id: String) = api.followUser(id)

    suspend fun unfollowUser(id: String) = api.unfollowUser(id)

    suspend fun getFollowers(id: Int) = api.getFollowers(id)

    suspend fun getFollowing(id: Int) = api.getFollowing(id)

    suspend fun getFollowingStatus(id: Int) = api.getFollowStatus(id)

}