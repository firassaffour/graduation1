package com.example.graduation1.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduation1.data.repository.UserRepository
import com.example.graduation1.domain.models.User
import kotlinx.coroutines.launch
import androidx.core.content.edit
import com.example.graduation1.friendsList
import com.example.graduation1.language
import com.example.graduation1.user
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _currentUser = MutableStateFlow<User>(user)
    val currentUser  = _currentUser.asStateFlow()

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users  = _users.asStateFlow()

    private val _followers = MutableStateFlow<List<User>>(emptyList())
    val followers  = _followers.asStateFlow()

    private val _following = MutableStateFlow<List<User>>(emptyList())
    val following  = _following.asStateFlow()

    var selectedUser by mutableStateOf<User?>(null)

        private set
    init {
        getUsers()
        getCurrentUser()
    }

    private fun getUsers(){
        viewModelScope.launch {
            try {
                _users.value = userRepository.getUsers()
                Log.d("userViewModel", "loadUsers: $users")
            }
            catch (e: Exception){
                Log.e("userViewModel", "loadUsers: ${e.message}")
            }
        }
    }

    private fun getCurrentUser(){
        viewModelScope.launch {
            try {
                _currentUser.value = userRepository.getCurrentUser()

                Log.d("userViewModel", "getCurrentUser: ${_currentUser.value}")
            }
            catch (e: Exception){
                Log.e("userViewModel", "getCurrentUser: ${e.message}")
            }
        }
    }

    fun getFollowers(followersList : List<String>){
        viewModelScope.launch {
            try {
                _followers.value = _users.value.filter { it.id in followersList }
            }
            catch (e: Exception){
                Log.d("userViewModel", "followers: $users")
                Log.e("userViewModel", "followers: ${e.message}")
            }
        }
    }

    fun getFollowing(followingList : List<String>){
        viewModelScope.launch {
            try {
                _following.value = _users.value.filter { it.id in followingList }
                Log.d("userViewModel", "following: $users")
            }
            catch (e: Exception){
                Log.e("userViewModel", "following: ${e.message}")
            }
        }
    }

    fun getUserDetails(userId : String) : User?{
        return _users.value.find { it.id == userId }
    }

    fun editUser(id: String, user: User){
        viewModelScope.launch {
            try {
                userRepository.editUser(id, user)
            }
            catch (e: Exception){
                Log.e("userViewModel", "friendsViewModel: ${e.message}")
            }
        }
    }

    fun followUser(userId: String){
        viewModelScope.launch {
            try {
                val currentUserId = _currentUser.value.id ?: return@launch
                val isFollowing = _users.value.first { it.id == userId }.followersList.contains(currentUserId)
                _users.value =_users.value.map {
                    if (it.id == userId) {
                        if (isFollowing) it.copy(followersList = it.followersList - currentUserId)

                        else it.copy(followersList = it.followersList + currentUserId)
                    }
                    else it
                }

                _currentUser.value = _currentUser.value.copy(
                    followingList =
                        if (isFollowing) _currentUser.value.followingList - userId

                        else _currentUser.value.followingList - userId
                )
            }
            catch (e: Exception){
                Log.e("userViewModel", "friendsViewModel: ${e.message}")
            }
        }
    }

    fun setAppLanguage(context : Context,languages : String){
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        prefs.edit() { putString("lang", languages) }
        language = languages

        val locale = Locale(languages)
        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)

        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    fun refreshData(){
        getUsers()
        getCurrentUser()
    }
}