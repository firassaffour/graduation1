package com.example.graduation1.data.repository

import com.example.graduation1.data.remote.ApiService
import com.example.graduation1.domain.models.requets_response.MessageResponse
import com.example.graduation1.domain.models.requets_response.SendMessageRequest

class ChatRepository(private val api : ApiService) {

    suspend fun getInbox(): List<MessageResponse>{return api.getInbox()}

    suspend fun getConversation(otherUserId: Int): List<MessageResponse>{return api.getConversation(otherUserId)}

    suspend fun sendMessage(request: SendMessageRequest): MessageResponse{return api.sendMessage(request)}

    suspend fun deleteMessage(id: Int) {return api.deleteMessage(id)}
}