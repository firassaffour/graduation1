package com.example.graduation1.data.repository

import com.example.graduation1.data.remote.ApiService
import com.example.graduation1.domain.models.requets_response.AuthResponse
import com.example.graduation1.domain.models.requets_response.LoginRequest
import com.example.graduation1.domain.models.requets_response.RegisterRequest

class AuthRepository(private val api : ApiService) {

    suspend fun createAccount(registerRequest: RegisterRequest) = api.createAccount(registerRequest)

    suspend fun login(email : String, password : String) : AuthResponse {
        return api.login(LoginRequest(email, password))
    }
}