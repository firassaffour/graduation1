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
import com.example.graduation1.messageList2
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
    }

    fun getChatContent(chatId: String){
        viewModelScope.launch {
            try {
                _chatsList.value.map {
                    if (it.chatId == chatId)
                        _chatContent.value = it.messagesList

                    else it
                }
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
     fun sendMessage(chatId : String ,messageText: String, image: String?){
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
                _chatsList.value = _chatsList.value.map { chat ->
                    if (chat.chatId == chatId)
                        chat.copy(messagesList = chat.messagesList + message)
                    else chat
                }
                _chatContent.value = _chatsList.value.first { it.chatId == chatId }.messagesList
            }
            catch (e: Exception){
                Log.e("API", "ChatViewModel: ${e.message}")
            }
        }
    }

    fun getUnSeenMessagesCount(chatId : String) : Int{
        val chat = _chatsList.value.find { it.chatId ==  chatId}
        return chat?.messagesList?.count { it.senderId != currentUser.id && !it.isSeen } ?: 0
    }

    fun updateMessagesSeen(chatId: String){
        viewModelScope.launch {
            try {
                _chatsList.value = _chatsList.value.map { chat ->
                    if (chat.chatId == chatId){
                        chat.copy(messagesList = chat.messagesList.map { message ->
                            if (message.senderId != currentUser.id)
                                message.copy(isSeen = true)
                            else message
                        })
                    }
                    else chat
                }
            }
            catch (e: Exception){
                Log.e("API", "ChatViewModel: ${e.message}")
            }
        }
    }
}