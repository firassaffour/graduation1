package com.example.graduation1.domain.models

data class Message(
    val messageId : String = "",
    val text : String = "",
    val senderId : String = "",
    val createdAt : Long = 0,
    val image : String? = null,
    val isSeen : Boolean = false
)