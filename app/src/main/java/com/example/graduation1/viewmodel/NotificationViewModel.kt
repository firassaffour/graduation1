package com.example.graduation1.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduation1.data.repository.NotificationRepository
import com.example.graduation1.data.repository.UserRepository
import com.example.graduation1.domain.models.Notification
import com.example.graduation1.domain.models.User
import com.example.graduation1.domain.models.requets_response.NotificationResponse
import com.example.graduation1.domain.models.requets_response.UnreadCountResponse
import com.example.graduation1.language
import com.example.graduation1.todayNotificationList
import com.example.graduation1.user
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@RequiresApi(Build.VERSION_CODES.O)
class NotificationViewModel(private val notificationRepository: NotificationRepository, private val userRepository: UserRepository) : ViewModel() {

    private val _todayNotifications = MutableStateFlow<List<NotificationResponse>>(emptyList())
    val todayNotifications = _todayNotifications.asStateFlow()

    private val _lastWeeksNotifications = MutableStateFlow<List<NotificationResponse>>(emptyList())
    val lastWeeksNotifications = _lastWeeksNotifications.asStateFlow()

    private val _notifications = MutableStateFlow<List<NotificationResponse>>(emptyList())
    val notifications = _notifications.asStateFlow()

    private val _currentUser = MutableStateFlow<User>(user)
    val currentUser  = _currentUser.asStateFlow()

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount = _unreadCount.asStateFlow()

    init {
        getNotifications()
        getCurrentUser()
        getUnreadCount()
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNotificationDate(createdAt : String) : Boolean{
        val time = LocalDateTime.parse(createdAt)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val diff = System.currentTimeMillis() - time

        return diff < (1000 * 60 * 60 * 24)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNotifications(){
        viewModelScope.launch {
            try {
                _notifications.value = notificationRepository.getNotification()
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

    /**
     * Send a real notification via the backend API.
     *
     * Call this when:
     *  - A user follows another user  → type = "follow",  message = "$name started following you"
     *  - A user comments on a post   → type = "comment", message = "$name commented on your post"
     *  - A user likes a post         → type = "like",    message = "$name liked your post"
     *  - A user joins a group        → type = "join",    message = "$name joined your group"
     *
     * Example from OtherUsersProfileScreen follow button:
     *   notificationViewModel.sendNotification(
     *       userId  = user.id.toInt(),
     *       type    = "follow",
     *       message = "${currentUser.name} started following you"
     *   )
     */
    fun sendNotification(
        userId: Int,
        groupId: Int? = null,
        type: String? = null,
        message: String? = null
    ) {
        viewModelScope.launch {
            try {
                val response = notificationRepository.sendNotification(userId, groupId, type, message)
                // Optimistically add to local list so sender sees it too
                _notifications.value = listOf(response) + _notifications.value
                Log.d("NotificationVM", "sendNotification OK: ${response.notificationID}")
            } catch (e: Exception) {
                Log.e("NotificationVM", "sendNotification: ${e.message}")
            }
        }
    }

    private fun getUnreadCount(){
        viewModelScope.launch {
            try {
                _unreadCount.value = notificationRepository.getUnreadCount().unreadCount
                Log.d("API", "getUnreadCount success: ${_unreadCount.value} ")
            }
            catch (e : Exception){
                Log.e("API", "getUnreadCount error: ${e.message}", )
            }
        }
    }

    fun markNotificationRead(id: Int){
        viewModelScope.launch {
            try {
                val response = notificationRepository.markNotificationRead(id)
                Log.d("API", "markNotificationRead success: $response")
            }
            catch (e : Exception){
                Log.e("API", "markNotificationRead error: ${e.message}", )
            }
        }
    }

    fun markAllNotificationsRead() {
        viewModelScope.launch {
            try {
                val response = notificationRepository.markAllNotificationsRead()
                Log.d("API", "markAllNotificationsRead success: $response")
            }
            catch (e : Exception){
                Log.e("API", "markAllNotificationsRead error: ${e.message}", )
            }
        }
    }

    fun deleteNotification(id: Int){
        viewModelScope.launch {
            try {
                val response = notificationRepository.deleteNotification(id)
                Log.d("API", "deleteNotification success: $response")
            }
            catch (e : Exception){
                Log.e("API", "deleteNotification error: ${e.message}", )
            }
        }
    }

    fun getTimeAgo(createdAt : String) : String{
        val time = LocalDateTime.parse(createdAt)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val diff = System.currentTimeMillis() - time

        val seconds = diff / 1000
        val minutes = diff / (1000 * 60)
        val hours = diff / (1000 * 60 * 60)
        val days = diff / (1000 * 60 * 60 * 24)

        val secondsText = if (language == "en") "now" else "الأن"
        val minutesText = if (language == "en") "$minutes minutes" else "$minutes دقيقة "
        val hoursText = if (language == "en") "$hours hours" else "$hours ساعة "
        val daysText = if (language == "en") "$days days" else "$days يوم "

        return when{
            seconds < 60 -> secondsText
            minutes < 60 -> minutesText
            hours < 24 -> hoursText
            days < 7 -> daysText
            else -> daysText
        }
    }
}