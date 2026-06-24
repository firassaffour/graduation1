package com.example.graduation1.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduation1.data.repository.AuthRepository
import com.example.graduation1.data.repository.ChatbotRepository
import com.example.graduation1.data.repository.UserRepository
import com.example.graduation1.domain.models.Message
import com.example.graduation1.domain.models.User
import com.example.graduation1.domain.models.requets_response.ChatMessageResponse
import com.example.graduation1.domain.models.requets_response.ChatSessionResponse
import com.example.graduation1.domain.models.requets_response.CodeSubmissionResponse
import com.example.graduation1.domain.models.requets_response.CodeSubmitRequest
import com.example.graduation1.domain.models.requets_response.CodeSubmitResult
import com.example.graduation1.messageList
import com.example.graduation1.user
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ChatbotViewModel(private val repository: ChatbotRepository, private val userRepository: UserRepository) : ViewModel() {

    /** Messages shown in the current conversation (user + assistant bubbles). */
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    /** The text the user is typing right now. */
    private val _messageText = MutableStateFlow("")
    val messageText = _messageText.asStateFlow()

    /** True while we are waiting for the AI to reply. */
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    /** Non-null when something went wrong; shown as a snackbar / toast. */
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    /** All sessions for the history screen. */
    private val _sessions = MutableStateFlow<List<ChatSessionResponse>>(emptyList())
    val sessions = _sessions.asStateFlow()

    /** The session that is currently open. */
    private val _activeSession = MutableStateFlow<ChatSessionResponse?>(null)
    val activeSession = _activeSession.asStateFlow()

    /** The logged-in user (needed to decide which bubble side to use). */
    private val _currentUser = MutableStateFlow(User())
    val currentUser = _currentUser.asStateFlow()

    init {
       loadCurrentUser()
    }

    private fun getCurrentUser(){
        viewModelScope.launch {
            try {
                _currentUser.value = userRepository.getCurrentUser()
                Log.d("userViewModel", "loadUsers: ${_currentUser.value}")
            }
            catch (e: Exception){
                Log.e("userViewModel", "loadUsers: ${e.message}")
            }
        }
    }


    fun updateMessageText(messageText: String){
        _messageText.value = messageText
    }



    // ── Init ────────────────────────────────────────────────────────────────

    private fun loadCurrentUser() {
        viewModelScope.launch {
            try {
                _currentUser.value = userRepository.getCurrentUser()
            } catch (e: Exception) {
                Log.e("ChatbotVM", "loadCurrentUser: ${e.message}")
            }
        }
    }

    // ── Session management ──────────────────────────────────────────────────

    /**
     * Call this when the user taps "New Chat" or opens the chatbot screen
     * for the first time. It creates a fresh session on the backend and
     * clears the message list.
     */
    fun startNewSession(title: String? = null) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val session = repository.newSession(title)
                _activeSession.value = session
                _messages.value = emptyList()
                // Add the new session to the history list
                _sessions.value = listOf(session) + _sessions.value
                Log.d("ChatbotVM", "New session: ${session.sessionID}")
            } catch (e: Exception) {
                _error.value = "Could not start a new chat. Check your connection."
                Log.e("ChatbotVM", "startNewSession: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Open a past session from the history screen and load its messages.
     */
    fun openSession(session: ChatSessionResponse) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _activeSession.value = session
                val history = repository.getHistory(session.sessionID)
                _messages.value = history.map { it.toUiMessage() }
                Log.d("ChatbotVM", "Loaded ${history.size} messages for session ${session.sessionID}")
            } catch (e: Exception) {
                _error.value = "Could not load chat history."
                Log.e("ChatbotVM", "openSession: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ── Messaging ───────────────────────────────────────────────────────────

    /**
     * Send the current [messageText] to the backend and append both the
     * user bubble and the AI reply to [messages].
     *
     * If no session is open yet, we create one automatically.
     */
    fun sendMessage() {
        val text = _messageText.value.trim()
        if (text.isEmpty()) return

        viewModelScope.launch {
            try {
                Log.e("CHATBOT", "1", )
                // 1. Ensure we have an active session
                val session = _activeSession.value ?: run {
                    Log.e("CHATBOT", "2", )
                    val s = repository.newSession()
                    Log.e("CHATBOT", "3 ${s.sessionID}", )
                    _activeSession.value = s
                    _sessions.value = listOf(s) + _sessions.value
                    s
                }

                Log.e("CHATBOT", "4", )

                // 2. Optimistically add the user bubble immediately
                val userBubble = Message(
                    messageId  = "tmp_${System.currentTimeMillis()}",
                    text       = text,
                    senderId   = _currentUser.value.id,
                    createdAt  = System.currentTimeMillis()
                )
                _messages.value = _messages.value + userBubble
                _messageText.value = ""

                // 3. Show loading indicator
                _isLoading.value = true

                // 4. Send to backend
                val response = repository.sendMessage(session.sessionID, text)

                Log.e("CHATBOT", "5 $response", )

                if (response.error != null) {
                    _error.value = "AI error: ${response.error}"
                    return@launch
                }
                Log.e("ChatbotVM", "sendMessage: ${_error.value}")

                // 5. Append the AI reply bubble
                val reply = response.reply ?: "(no reply)"
                val aiBubble = Message(
                    messageId = "ai_${System.currentTimeMillis()}",
                    text      = reply,
                    senderId  = "AI",   // anything that is NOT the user's id
                    createdAt = System.currentTimeMillis()
                )
                _messages.value = _messages.value + aiBubble

                Log.d("ChatbotVM", "sendMessage OK, reply: $reply")

            } catch (e: Exception) {
                _error.value = "Could not reach the server. Are you online?"
                Log.e("ChatbotVM", "sendMessage: ${e.message}")
                Log.e("ChatbotVM", "sendMessage: ${_error.value}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ── Text field ──────────────────────────────────────────────────────────

    fun clearError() {
        _error.value = null
    }

    // ── Helpers ─────────────────────────────────────────────────────────────

    /** Convert the backend ChatMessageResponse to the local Message UI model. */
    private fun ChatMessageResponse.toUiMessage() = Message(
        messageId = messageID.toString(),
        text      = content ?: "",
        // role is "user" or "assistant" – map "user" to the actual user's id,
        // everything else to "AI" so the bubble appears on the left.
        senderId  = if (role == "user") _currentUser.value.id else "AI",
        createdAt = System.currentTimeMillis() // we don't parse the date string here
    )

    fun getCodeSubmissions() {
        viewModelScope.launch {
            try {
                val response = repository.getCodeSubmissions()
                Log.d("API", "getCodeSubmissions: $response")
            }
            catch (e : Exception){
                Log.e("API", "getCodeSubmissions: ${e.message}", )
            }

        }
    }

    fun getCodeSubmissionById(id: Int){
        viewModelScope.launch {
            try {
                val response = repository.getCodeSubmissionById(id)
                Log.d("API", "getCodeSubmissionById: $response")
            }
            catch (e : Exception){
                Log.e("API", "getCodeSubmissionById: ${e.message}", )
            }
        }
    }

    fun submitCode(request: CodeSubmitRequest){
        viewModelScope.launch {
            try {
                val response = repository.submitCode(request)
                Log.d("API", "submitCode: $response")
            }
            catch (e : Exception){
                Log.e("API", "submitCode: ${e.message}", )
            }

        }
    }
}