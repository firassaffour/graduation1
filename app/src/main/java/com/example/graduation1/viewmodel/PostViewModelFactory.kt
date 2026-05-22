package com.example.graduation1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.graduation1.data.repository.PostRepository
import com.example.graduation1.data.repository.UserRepository

class PostViewModelFactory(private val postRepository: PostRepository, private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PostViewModel(postRepository, userRepository) as T
    }
}