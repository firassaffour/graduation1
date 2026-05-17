package com.example.graduation1.domain.models

data class Message(
    val messageId : String = "",
    val text : String = "",
    val senderId : String = "",
    val date : String = "",
    val image : String? = null,
    val isSeen : Boolean = false
)