package com.example.graduation1.domain.models

data class Notification(
    val notificationId : String = "",
    val text : String = "",
    val image : String = "",
    val createdAt : Long = 0
)