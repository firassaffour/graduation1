package com.example.graduation1.viewmodel

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduation1.chatList
import com.example.graduation1.data.repository.ChatRepository
import com.example.graduation1.data.repository.MediaRepository
import com.example.graduation1.data.repository.UserRepository
import com.example.graduation1.domain.models.ChatItem
import com.example.graduation1.domain.models.Message
import com.example.graduation1.domain.models.User
import com.example.graduation1.domain.models.requets_response.MessageResponse
import com.example.graduation1.domain.models.requets_response.SendMessageRequest
import com.example.graduation1.language
import com.example.graduation1.user
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ChatViewModel(private val chatRepository: ChatRepository, private val userRepository: UserRepository, private val mediaRepository: MediaRepository) : ViewModel() {

    private val _chatsList = MutableStateFlow<List<ChatItem>>(emptyList())
    val chatsList = _chatsList.asStateFlow()

    private val _chatContent = MutableStateFlow<List<Message>>(emptyList())
    val chatContent = _chatContent.asStateFlow()

    private val _currentUser = MutableStateFlow(user)
    val currentUser = _currentUser.asStateFlow()

    private val _allUsers = MutableStateFlow<List<User>>(emptyList())
    val allUsers = _allUsers.asStateFlow()

    private val _messageText = MutableStateFlow("")
    val messageText = _messageText.asStateFlow()

    private val _chatSearchQuery = MutableStateFlow("")
    val chatSearchQuery = _chatSearchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val BASE_URL = "https://graduation-project-backend-production-bc68.up.railway.app"

    // ── Init ────────────────────────────────────────────────────────────────

    init {
        viewModelScope.launch {
            loadCurrentUserSuspend()
            loadAllUsersSuspend()
            loadInboxInternal()
        }
    }

    private suspend fun loadCurrentUserSuspend() {
        try {
            _currentUser.value = userRepository.getCurrentUser()
        } catch (e: Exception) {
            Log.e("ChatVM", "loadCurrentUser: ${e.message}")
        }
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            try {
                _currentUser.value = userRepository.getCurrentUser()
            } catch (e: Exception) {
                Log.e("ChatVM", "loadCurrentUser: ${e.message}")
            }
        }
    }

    private suspend fun loadAllUsersSuspend() {
        try {
            _allUsers.value = userRepository.getUsers()
            Log.d("ChatVM", "allUsers loaded: ${_allUsers.value.size}")
        } catch (e: Exception) {
            Log.e("ChatVM", "loadAllUsers: ${e.message}")
        }
    }

    // ── Inbox ────────────────────────────────────────────────────────────────

    fun loadInbox() {
        viewModelScope.launch {
            loadCurrentUserSuspend()
            loadInboxInternal()
        }
    }

    private suspend fun loadInboxInternal() {
        try {
            _isLoading.value = true
            val inbox = chatRepository.getInbox()
            val myId  = _currentUser.value.id.toIntOrNull()

            val grouped = inbox
                .groupBy { msg ->
                    if (msg.senderID == myId) msg.receiverID?.toString() ?: "unknown"
                    else msg.senderID?.toString() ?: "unknown"
                }
                .filter { it.key != "unknown" && it.key != "null" }
                .map { (partnerId, messages) ->
                    ChatItem(
                        chatId       = partnerId,
                        userId       = partnerId,
                        messagesList = messages
                            .sortedBy { parseTime(it.sentAt) }
                            .map { it.toMessage() }
                    )
                }
                .sortedByDescending { it.messagesList.lastOrNull()?.createdAt ?: 0L }

            _chatsList.value = grouped
            Log.d("ChatVM", "loadInbox OK: ${grouped.size} conversations (myId=$myId)")
        } catch (e: Exception) {
            _error.value = "Could not load chats."
            Log.e("ChatVM", "loadInboxInternal: ${e.message}")
        } finally {
            _isLoading.value = false
        }
    }

    // ── Conversation ─────────────────────────────────────────────────────────

    fun getChatContent(otherUserId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val id = otherUserId.toIntOrNull() ?: return@launch
                val messages = chatRepository.getConversation(id)
                    .sortedBy { parseTime(it.sentAt) }
                    .map {
                        val media = mediaRepository.getMediaByPost(it.messageID)
                        val image = media.find { !it.filePath.isNullOrEmpty() }?.filePath ?: ""
                        it.toMessage()}

                _chatContent.value = messages

                _chatsList.value = _chatsList.value.map { chat ->
                    if (chat.chatId == otherUserId) chat.copy(messagesList = messages)
                    else chat
                }.let { existing ->
                    if (existing.none { it.chatId == otherUserId } && messages.isNotEmpty())
                        listOf(ChatItem(chatId = otherUserId, userId = otherUserId, messagesList = messages)) + existing
                    else existing
                }

                Log.d("ChatVM", "getChatContent: ${messages.size} messages with $otherUserId")
            } catch (e: Exception) {
                _error.value = "Could not load messages."
                Log.e("ChatVM", "getChatContent: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }


    // ── Send message ─────────────────────────────────────────────────────────

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessage(otherUserId: String, text: String, imageUri: Uri?) {
        val trimmed = text.trim()
        if (trimmed.isEmpty() && imageUri == null) return

        viewModelScope.launch {
            try {
                val receiverId = otherUserId.toIntOrNull() ?: return@launch

                val content = trimmed
                val response = chatRepository.sendMessage(
                    SendMessageRequest(receiverID = receiverId, content = content)
                )

                var imageUrl: String? = null
                if (imageUri != null) {
                    val media = mediaRepository.uploadImage(imageUri, messageId = response.messageID)
                    imageUrl = media.filePath
                }

                val optimistic = Message(
                    messageId = "tmp_${System.currentTimeMillis()}",
                    text      = trimmed,
                    senderId  = _currentUser.value.id,
                    createdAt = System.currentTimeMillis(),
                    image     = imageUrl,
                    isSeen    = false
                )
                appendToConversation(otherUserId, optimistic)
                _messageText.value = ""

                val real = response.toMessage()
                _chatContent.value = _chatContent.value.map {
                    if (it.messageId == optimistic.messageId) real else it
                }
                _chatsList.value = _chatsList.value.map { chat ->
                    if (chat.chatId == otherUserId)
                        chat.copy(messagesList = chat.messagesList.map {
                            if (it.messageId == optimistic.messageId) real else it
                        })
                    else chat
                }

                Log.d("ChatVM", "sendMessage OK: ${response.messageID}")
            } catch (e: Exception) {
                _error.value = "Message failed to send."
                Log.e("ChatVM", "sendMessage: ${e.message}")
            }
        }
    }

    // ── Delete message ────────────────────────────────────────────────────────

    fun removeMessage(messageId: String, otherUserId: String) {
        _chatContent.value = _chatContent.value.filter { it.messageId != messageId }
        _chatsList.value = _chatsList.value.map { chat ->
            if (chat.chatId == otherUserId)
                chat.copy(messagesList = chat.messagesList.filter { it.messageId != messageId })
            else chat
        }

        viewModelScope.launch {
            try {
                val response = chatRepository.deleteMessage(messageId.toInt())
            } catch (e: Exception) {
                Log.e("ChatVM", "removeMessage: ${e.message}")
                getChatContent(otherUserId)
            }
        }
    }

    // ── Seen status ───────────────────────────────────────────────────────────

    fun updateMessagesSeen(otherUserId: String) {
        _chatsList.value = _chatsList.value.map { chat ->
            if (chat.chatId == otherUserId) {
                chat.copy(messagesList = chat.messagesList.map { msg ->
                    if (msg.senderId != _currentUser.value.id) msg.copy(isSeen = true)
                    else msg
                })
            } else chat
        }
        _chatContent.value = _chatContent.value.map { msg ->
            if (msg.senderId != _currentUser.value.id) msg.copy(isSeen = true)
            else msg
        }
    }

    fun getUnSeenMessagesCount(otherUserId: String): Int {
        val chat = _chatsList.value.find { it.chatId == otherUserId }
        return chat?.messagesList?.count {
            it.senderId != _currentUser.value.id && !it.isSeen
        } ?: 0
    }


    fun openConversationWith(otherUserId: String) {
        _chatContent.value = emptyList()
        getChatContent(otherUserId)
        updateMessagesSeen(otherUserId)
    }



    // ── Helpers ──────────────────────────────────────────────────────────────

    private fun appendToConversation(otherUserId: String, message: Message) {
        _chatContent.value = _chatContent.value + message

        val existing = _chatsList.value.find { it.chatId == otherUserId }
        _chatsList.value = if (existing != null) {
            _chatsList.value.map { chat ->
                if (chat.chatId == otherUserId)
                    chat.copy(messagesList = chat.messagesList + message)
                else chat
            }
        } else {
            listOf(ChatItem(chatId = otherUserId, userId = otherUserId, messagesList = listOf(message))) +
                    _chatsList.value
        }
    }

    private fun parseTime(sentAt: String?): Long {
        if (sentAt.isNullOrBlank()) return 0L
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDateTime.parse(sentAt)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
            } else 0L
        } catch (e: Exception) { 0L }
    }

    private fun MessageResponse.toMessage(): Message {
        val myId = _currentUser.value.id
        return Message(
            messageId = messageID.toString(),
            text      = content ?: "",
            senderId  = senderID?.toString() ?: "",
            createdAt = parseTime(sentAt).let { if (it == 0L) System.currentTimeMillis() else it },
            image     = null,   // backend doesn't return image in message — use filePath from media if needed
            isSeen    = isSeen
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getMessageTime(createdAt: Long): String {
        val locale    = if (language == "en") Locale.ENGLISH else Locale("ar")
        val formatter = DateTimeFormatter.ofPattern("hh:mm a", locale)
        val dateTime  = Instant.ofEpochMilli(createdAt)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
        return dateTime.format(formatter)
    }

    fun updateMessageText(text: String) { _messageText.value = text }
    fun updateChatSearchQuery(q: String) { _chatSearchQuery.value = q }
    fun clearError() { _error.value = null }

    fun refreshData(userId : String) {
        loadInbox()
        getChatContent(userId)
        loadCurrentUser()
    }
}