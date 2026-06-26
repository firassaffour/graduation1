package com.example.graduation1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.graduation1.data.repository.GroupsRepository
import com.example.graduation1.data.repository.MediaRepository
import com.example.graduation1.data.repository.UserRepository

class GroupsViewModelFactory(private val groupsRepository: GroupsRepository, private val userRepository: UserRepository, private val mediaRepository: MediaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GroupsViewModel(groupsRepository, userRepository, mediaRepository) as T
    }
}