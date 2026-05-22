package com.example.graduation1.data.repository

import com.example.graduation1.data.remote.RetrofitInstance

object AppModule {

    val userRepository = UserRepository(RetrofitInstance.api)
    val authRepository = AuthRepository(RetrofitInstance.api)
    val chatbotRepository = ChatbotRepository(RetrofitInstance.api)
    val groupsRepository = GroupsRepository(RetrofitInstance.api)
    val notificationRepository = NotificationRepository(RetrofitInstance.api)
    val postRepository = PostRepository(RetrofitInstance.api)
    val chatRepository = ChatRepository(RetrofitInstance.api)
}