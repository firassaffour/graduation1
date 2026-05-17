package com.example.graduation1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.graduation1.data.repository.AuthRepository
import com.example.graduation1.data.repository.ChatbotRepository

class ChatbotViewModelFactory(private val repository: ChatbotRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatbotViewModel(repository) as T
    }
}