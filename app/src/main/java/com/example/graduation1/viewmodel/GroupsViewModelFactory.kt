package com.example.graduation1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.graduation1.data.repository.GroupsRepository
import com.example.graduation1.data.repository.UserRepository

class GroupsViewModelFactory(private val groupsRepository: GroupsRepository, private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GroupsViewModel(groupsRepository, userRepository) as T
    }
}