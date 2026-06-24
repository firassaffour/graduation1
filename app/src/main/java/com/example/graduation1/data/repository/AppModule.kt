package com.example.graduation1.data.repository

import android.content.Context
import com.example.graduation1.data.remote.RetrofitInstance

object AppModule {

    // Call AppModule.initialize(context) from your Application class or MainActivity
    private lateinit var appContext: Context

    fun initialize(context: Context) {
        appContext = context.applicationContext
        RetrofitInstance.initialize(context)
    }

    val userRepository = UserRepository(RetrofitInstance.api)
    val authRepository = AuthRepository(RetrofitInstance.api)
    val chatbotRepository = ChatbotRepository(RetrofitInstance.api)
    val groupsRepository = GroupsRepository(RetrofitInstance.api)
    val notificationRepository = NotificationRepository(RetrofitInstance.api)
    val postRepository = PostRepository(RetrofitInstance.api)
    val chatRepository = ChatRepository(RetrofitInstance.api)
    val mediaRepository        by lazy { MediaRepository(RetrofitInstance.api, appContext) }
}