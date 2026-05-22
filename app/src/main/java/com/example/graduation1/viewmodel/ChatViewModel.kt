package com.example.graduation1.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduation1.chatList
import com.example.graduation1.data.repository.ChatRepository
import com.example.graduation1.data.repository.UserRepository
import com.example.graduation1.domain.models.ChatItem
import com.example.graduation1.domain.models.Message
import com.example.graduation1.domain.models.User
import com.example.graduation1.messageList2
import com.example.graduation1.user
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ChatViewModel(private val chatRepository: ChatRepository, private val userRepository: UserRepository) : ViewModel() {


    private val _chatContent =  MutableStateFlow<List<Message>>(emptyList())
    val chatContent  = _chatContent.asStateFlow()

    private val _chatsList = MutableStateFlow<List<ChatItem>>(emptyList())
    val chatsList = _chatsList.asStateFlow()

    private val _messageText = MutableStateFlow("")
    val messageText = _messageText.asStateFlow()

    private val _currentUser = userRepository.currentUser
    val currentUser  = _currentUser

    init {
        getChats()
        getUnSeenMessagesCount()
    }

    fun getChatContent(userId: String){
        viewModelScope.launch {
            try {
                _chatContent.value = messageList2
            }
            catch (e: Exception){
                Log.e("API", "ChatViewModel: ${e.message}")
            }
        }
    }

    private fun getChats(){
        viewModelScope.launch {
            try {
                _chatsList.value = chatList
            }
            catch (e: Exception){
                Log.e("API", "ChatViewModel: ${e.message}")
            }
        }
    }

    fun updateMessageText(messageText: String){
        _messageText.value = messageText
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
                    _currentUser.id,
                    formatted,
                    image
                    )
                _chatContent.value = _chatContent.value + message
            }
            catch (e: Exception){
                Log.e("API", "ChatViewModel: ${e.message}")
            }
        }
    }

    fun getUnSeenMessagesCount(){
        viewModelScope.launch {
            try {
                _chatContent.value.map { message ->
                    if (!message.isSeen){
                        _chatsList.value.map {  user ->
                            if (user.id == message.senderId) user.copy(unSeenMessagesCount = user.unSeenMessagesCount + 1)

                            else user
                        }
                    }

                    else message
                }
            }
            catch (e: Exception){
                Log.e("API", "ChatViewModel: ${e.message}")
            }
        }
    }

    fun updateMessagesSeen(userId : String){
        viewModelScope.launch {
            try {
                _chatContent.value.map {
                    if (it.senderId == userId)
                        it.copy(isSeen = true)

                    else it
                }
            }
            catch (e: Exception){
                Log.e("API", "ChatViewModel: ${e.message}")
            }
        }
    }
}