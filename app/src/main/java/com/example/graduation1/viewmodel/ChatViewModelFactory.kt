package com.example.graduation1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.graduation1.data.repository.ChatRepository
import com.example.graduation1.data.repository.UserRepository

class ChatViewModelFactory(private val chatRepository: ChatRepository, private  val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatViewModel(chatRepository, userRepository) as T
    }
}