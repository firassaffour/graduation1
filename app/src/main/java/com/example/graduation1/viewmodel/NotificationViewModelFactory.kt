package com.example.graduation1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.graduation1.data.repository.NotificationRepository
import com.example.graduation1.data.repository.UserRepository

class NotificationViewModelFactory(private val notificationRepository: NotificationRepository, private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NotificationViewModel(notificationRepository, userRepository) as T
    }
}