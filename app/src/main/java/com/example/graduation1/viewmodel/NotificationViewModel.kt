package com.example.graduation1.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduation1.data.repository.NotificationRepository
import com.example.graduation1.domain.models.Notification
import com.example.graduation1.lastWeekNotificationList
import com.example.graduation1.todayNotificationList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotificationViewModel(private val repository: NotificationRepository) : ViewModel() {

    private val _todayNotifications = MutableStateFlow<List<Notification>>(emptyList())
    val todayNotifications = _todayNotifications.asStateFlow()

    private val _lastWeekNotifications = MutableStateFlow<List<Notification>>(emptyList())
    val lastWeekNotifications = _lastWeekNotifications.asStateFlow()

    init {
        getNotifications()
    }

    private fun getNotifications(){
        viewModelScope.launch {
            try {
                _todayNotifications.value = todayNotificationList
                _lastWeekNotifications.value = lastWeekNotificationList
                //notificationList = repository.getNotification()
            }
            catch (e: Exception){
                Log.e("API", "NotificationViewModel: ${e.message}")
            }
        }
    }

    fun getNotificationCount() : Int{
        return _todayNotifications.value.count() + _lastWeekNotifications.value.count()
    }
}