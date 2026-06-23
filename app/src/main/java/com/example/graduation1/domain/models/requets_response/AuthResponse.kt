package com.example.graduation1.domain.models.requets_response

data class AuthResponse(
    val token : String,
    val userID : Int,
    val firstName : String?,
    val lastName : String?,
    val profileImage : String?,
    val message : String
)