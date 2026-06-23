package com.example.graduation1.domain.models.requets_response

data class RegisterRequest(
    val firstName : String = "",
    val lastName : String = "",
    val email : String = "",
    val password : String = "",
    val role : String = ""
)