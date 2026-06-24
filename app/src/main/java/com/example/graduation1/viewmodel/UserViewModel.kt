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
import com.example.graduation1.domain.models.requets_response.UpdateUserRequest
import com.example.graduation1.emptyProfileImage
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

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

    var selectedUser by mutableStateOf<User?>(null)

        private set
    init {
        getUsers()
        getCurrentUser()
    }

    private fun getUsers(){
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _users.value = userRepository.getUsers().map {
                    it.copy(followingList = userRepository.getFollowing(it.id.toInt()),
                        followersList = userRepository.getFollowers(it.id.toInt()),
                        isFollowedByMe = userRepository.getFollowingStatus(it.id.toInt()).isFollowing)
                }
                Log.d("userViewModel", "loadUsers: $users")
            }
            catch (e: Exception){
                Log.e("userViewModel", "loadUsers: ${e.message}")
            }
            finally {
                _isLoading.value = false
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

    fun editUser(id: String, name : String, userName : String, bio : String, profileImage : String?, onSuccess : () -> Unit){
        viewModelScope.launch {
            try {
                val userData = UpdateUserRequest(
                    firstName = name,
                    lastName = userName,
                    bio = bio,
                    profileImage = if (profileImage.isNullOrEmpty()) emptyProfileImage else profileImage
                )
                val response = userRepository.editUser(id, userData)

                onSuccess()

                Log.e("userViewModel", "edit user success $response")
            }
            catch (e: Exception){
                onSuccess()
                Log.e("userViewModel", "edit user failed: ${e.message}")
            }
        }
    }

    fun followUser(userId: String){
        viewModelScope.launch {
            try {
                _users.value =_users.value.map {
                    if (it.id == userId) {
                        if (it.isFollowedByMe) it.copy(isFollowedByMe = false)

                        else it.copy(isFollowedByMe = true)
                    }
                    else it
                }

                val response = userRepository.followUser(userId)

                Log.d("API", "followUser success: $response")
            }
            catch (e: Exception){
                Log.e("userViewModel", "follow user failed: ${e.message}")
            }
        }
    }

    fun unfollowUser(userId: String){
        viewModelScope.launch {
            try {
                userRepository.unfollowUser(userId)
                Log.d("API", "unfollowUser success: ")
            }
            catch (e : Exception){
                Log.e("API", "unfollowUser failed: ${e.message}")
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