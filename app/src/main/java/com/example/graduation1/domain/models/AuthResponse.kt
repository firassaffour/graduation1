package com.example.graduation1.domain.models

data class AuthResponse(
    val userId : Int,
    val firstName : String?,
    val lastName : String?,
    val email : String?
)