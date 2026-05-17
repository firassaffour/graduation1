package com.example.graduation1.data.repository

import com.example.graduation1.data.remote.ApiService

class NotificationRepository(private val api : ApiService) {

    suspend fun getNotification() = api.getNotification()
}