package com.example.graduation1.data.repository

import android.content.Context
import com.example.graduation1.data.remote.RetrofitInstance

object AppModule {
    private lateinit var appContext: Context

    fun initialize(context: Context) {
        appContext = context.applicationContext
        RetrofitInstance.initialize(context)
    }

    val userRepository         by lazy { UserRepository(RetrofitInstance.api) }
    val authRepository         by lazy { AuthRepository(RetrofitInstance.api) }
    val chatbotRepository      by lazy { ChatbotRepository(RetrofitInstance.api) }
    val groupsRepository       by lazy { GroupsRepository(RetrofitInstance.api) }
    val notificationRepository by lazy { NotificationRepository(RetrofitInstance.api) }
    val postRepository         by lazy { PostRepository(RetrofitInstance.api) }
    val chatRepository         by lazy { ChatRepository(RetrofitInstance.api) }
    val mediaRepository        by lazy { MediaRepository(RetrofitInstance.api, appContext) }
}