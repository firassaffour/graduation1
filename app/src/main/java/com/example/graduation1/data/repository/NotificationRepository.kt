package com.example.graduation1.data.repository

import com.example.graduation1.data.remote.ApiService
import com.example.graduation1.domain.models.requets_response.NotificationResponse
import com.example.graduation1.domain.models.requets_response.UnreadCountResponse

class NotificationRepository(private val api : ApiService) {

    suspend fun getNotification() : List<NotificationResponse> { return api.getNotifications() }

    suspend fun getUnreadCount(): UnreadCountResponse {return api.getUnreadCount()}

    suspend fun markNotificationRead(id: Int) = api.markNotificationRead(id)

    suspend fun markAllNotificationsRead() = api.markAllNotificationsRead()

    suspend fun deleteNotification(id: Int) = api.deleteNotification(id)
}