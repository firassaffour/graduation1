package com.example.graduation1.domain.models

data class RegisterRequest(
    val userID : Int = 0,
    val firstName : String = "",
    val lastName : String = "",
    val email : String = "",
    val password : String = "",
)