package com.example.graduation1.data.repository

import com.example.graduation1.data.remote.ApiService
import com.example.graduation1.domain.models.User
import com.example.graduation1.user

class UserRepository(private val api: ApiService) {

    val currentUser = user

    suspend fun getUsers() = api.getUsers()

    suspend fun getUserDetails(id : String) = api.getUserDetails(id)

    suspend fun editUser(id : String, user : User) = api.editUser(id, user)

}