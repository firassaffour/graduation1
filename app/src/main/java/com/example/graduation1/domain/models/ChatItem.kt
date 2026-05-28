package com.example.graduation1.domain.models

data class ChatItem(
    val chatId : String = "",
    val userId : String = "",
    val messagesList : List<Message> = emptyList()
)