package com.example.graduation1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.graduation1.data.repository.AuthRepository
import com.example.graduation1.data.repository.ChatbotRepository
import com.example.graduation1.data.repository.UserRepository

class ChatbotViewModelFactory(private val chatbotRepository: ChatbotRepository, private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatbotViewModel(chatbotRepository, userRepository) as T
    }
}