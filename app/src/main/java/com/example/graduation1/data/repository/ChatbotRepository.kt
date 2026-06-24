package com.example.graduation1.data.repository

import com.example.graduation1.data.remote.ApiService
import com.example.graduation1.domain.models.requets_response.ChatMessageResponse
import com.example.graduation1.domain.models.requets_response.ChatSendRequest
import com.example.graduation1.domain.models.requets_response.ChatSendResponse
import com.example.graduation1.domain.models.requets_response.ChatSessionResponse
import com.example.graduation1.domain.models.requets_response.CodeSubmissionResponse
import com.example.graduation1.domain.models.requets_response.CodeSubmitRequest
import com.example.graduation1.domain.models.requets_response.CodeSubmitResult
import com.example.graduation1.domain.models.requets_response.NewSessionRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

class ChatbotRepository(private val api : ApiService) {

    /** Create a new chat session. Returns sessionID, title, createdAt. */
    suspend fun newSession(title: String? = null): ChatSessionResponse =
        api.newChatSession(NewSessionRequest(title))

    /** Send a user message in a session. Returns the AI reply. */
    suspend fun sendMessage(sessionId: Int, message: String): ChatSendResponse =
        api.sendChatbotMessage(ChatSendRequest(sessionId, message))

    /** Load full message history for a session. */
    suspend fun getHistory(sessionId: Int): List<ChatMessageResponse> =
        api.getChatHistory(sessionId)

    suspend fun getCodeSubmissions(): List<CodeSubmissionResponse>{ return api.getCodeSubmissions()}

    suspend fun getCodeSubmissionById(id: Int): CodeSubmissionResponse {return api.getCodeSubmissionById(id)}

    suspend fun submitCode(request: CodeSubmitRequest): CodeSubmitResult {return api.submitCode(request)}
}