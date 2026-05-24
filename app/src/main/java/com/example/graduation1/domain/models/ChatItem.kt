package com.example.graduation1.domain.models

data class ChatItem(
    val chatId : String = "",
    val userId : String = "",
    val lastMessageText : String = "",
    val lastMessageTime : String = "",
    val unSeenMessagesCount : Int = 0
)