package com.example.graduation1.domain.models

data class ChatItem(
    val id : String = "",
    val name : String = "",
    val image : Any = "",
    val lastMessageText : String = "",
    val lastMessageTime : String = "",
    var isOnline : Boolean = false,
    val unSeenMessagesCount : Int = 0
)