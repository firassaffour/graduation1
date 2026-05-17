package com.example.graduation1.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduation1.data.repository.AuthRepository
import com.example.graduation1.data.repository.ChatbotRepository
import com.example.graduation1.domain.models.Message
import com.example.graduation1.domain.models.User
import com.example.graduation1.messageList
import com.example.graduation1.user
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ChatbotViewModel(private val repository: ChatbotRepository) : ViewModel() {

    private val _chatContent = MutableStateFlow<List<Message>>(emptyList())
    val chatContent = _chatContent.asStateFlow()

    private val _messageText = MutableStateFlow("")
    val messageText = _messageText.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser  = _currentUser.asStateFlow()

    init {
        _chatContent.value = messageList
        _currentUser.value = user
    }

    @OptIn(ExperimentalUuidApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessage(messageText: String, image: String?){
        viewModelScope.launch {
            try {
                val time = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("hh:mm")
                val formatted = time.format(formatter)
                val message = Message(
                    Uuid.random().toString(),
                    messageText,
                    currentUser.value!!.id,
                    formatted,
                    image
                )
                _chatContent.value = _chatContent.value + message
            }
            catch (e: Exception){
                Log.e("API", "ChatbotViewModel: ${e.message}")
            }
        }
    }

    fun updateMessageText(messageText: String){
        _messageText.value = messageText
    }
}