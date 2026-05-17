package com.example.graduation1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.graduation1.data.repository.AuthRepository
import com.example.graduation1.data.repository.GroupsRepository

class GroupsViewModelFactory(private val repository: GroupsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GroupsViewModel(repository) as T
    }
}