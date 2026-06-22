package com.example.graduation1.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduation1.data.repository.NotificationRepository
import com.example.graduation1.data.repository.UserRepository
import com.example.graduation1.domain.models.Notification
import com.example.graduation1.domain.models.User
import com.example.graduation1.todayNotificationList
import com.example.graduation1.user
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class NotificationViewModel(private val notificationRepository: NotificationRepository, private val userRepository: UserRepository) : ViewModel() {

    private val _todayNotifications = MutableStateFlow<List<Notification>>(emptyList())
    val todayNotifications = _todayNotifications.asStateFlow()

    private val _lastWeeksNotifications = MutableStateFlow<List<Notification>>(emptyList())
    val lastWeeksNotifications = _lastWeeksNotifications.asStateFlow()

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications = _notifications.asStateFlow()

    private val _currentUser = MutableStateFlow<User>(user)
    val currentUser  = _currentUser.asStateFlow()

    init {
        getNotifications()
        getCurrentUser()
    }

    private fun getCurrentUser(){
        viewModelScope.launch {
            try {
                _currentUser.value = userRepository.getCurrentUser()
                Log.d("userViewModel", "loadUsers: ${_currentUser.value}")
            }
            catch (e: Exception){
                Log.e("userViewModel", "loadUsers: ${e.message}")
            }
        }
    }

    private fun getNotificationDate(createdAt : Long) : Boolean{
        val diff = System.currentTimeMillis() - createdAt

        return diff < (1000 * 60 * 60 * 24)
    }

    private fun getNotifications(){
        viewModelScope.launch {
            try {
                _notifications.value = todayNotificationList.filter { it.userId == _currentUser.value.id }
                _todayNotifications.value = _notifications.value.filter { getNotificationDate(it.createdAt) }
                _lastWeeksNotifications.value = _notifications.value.filter { !getNotificationDate(it.createdAt) }
                //notificationList = repository.getNotification()
            }
            catch (e: Exception){
                Log.e("API", "NotificationViewModel: ${e.message}")
            }
        }
    }

    fun getNotificationCount() : Int{
        return _todayNotifications.value.count() + _lastWeeksNotifications.value.count()
    }

    @OptIn(ExperimentalUuidApi::class)
    fun sendNotification(userId : String, groupId : String){
        val notificationIsSent = _notifications.value.any { it.userId == userId && it.groupId == groupId }
        viewModelScope.launch {
            try {
                val notification = Notification(
                    Uuid.random().toString(),
                    groupId,
                    userId,
                    System.currentTimeMillis()
                )
                if (notificationIsSent)_notifications.value = _notifications.value - notification
                else _notifications.value = listOf(notification) + _notifications.value

            }
            catch (e: Exception){
                Log.e("API", "notificationViewModel send notification: ${e.message}")
            }
        }
    }

    fun removeNotification(notificationId : String){
        val notification = _notifications.value.find { it.notificationId == notificationId } ?: return
        _notifications.value = _notifications.value - notification

        getNotifications()
    }
}