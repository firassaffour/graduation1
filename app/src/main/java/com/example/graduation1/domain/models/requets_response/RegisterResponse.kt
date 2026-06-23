package com.example.graduation1.domain.models.requets_response

data class RegisterResponse(
    val userID : Int = 0,
    val firstName : String = "",
    val lastName : String = "",
    val email : String = "",
    val bio : String = "",
    val profileImage : String = "",
)