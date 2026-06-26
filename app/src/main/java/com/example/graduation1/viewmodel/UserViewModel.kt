package com.example.graduation1.viewmodel

import android.content.Context
import android.net.Uri
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
import com.example.graduation1.data.repository.MediaRepository
import com.example.graduation1.domain.models.requets_response.UpdateUserRequest
import com.example.graduation1.emptyProfileImage
import com.example.graduation1.friendsList
import com.example.graduation1.language
import com.example.graduation1.user
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class UserViewModel(private val userRepository: UserRepository, private val mediaRepository: MediaRepository) : ViewModel() {

    private val _currentUser = MutableStateFlow<User>(user)
    val currentUser  = _currentUser.asStateFlow()

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users  = _users.asStateFlow()

    private val _followers = MutableStateFlow<List<User>>(emptyList())
    val followers  = _followers.asStateFlow()

    private val _following = MutableStateFlow<List<User>>(emptyList())
    val following  = _following.asStateFlow()

    private val _isLoading = MutableStateFlow<Boolean>(true)
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
                _users.value = userRepository.getUsers().map {
                    it.copy(isFollowedByMe = userRepository.getFollowingStatus(it.id.toInt()).isFollowing) }
                Log.d("userViewModel", "loadUsers success: $users")
            }
            catch (e: Exception){
                Log.e("userViewModel", "loadUsers failed: ${e.message}")
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

    /** Fetch real followers from backend and update [_followers]. */
    fun loadFollowers(userId: Int) {
        viewModelScope.launch {
            try {
                val items = userRepository.getFollowers(userId)
                _followers.value = items.map { item ->
                    User(
                        id    = item.userID.toString(),
                        name  = item.firstName,
                        userName = item.lastName,
                        image = item.profileImage ?: emptyProfileImage
                    )
                }
            } catch (e: Exception) {
                Log.e("UserVM", "loadFollowers: ${e.message}")
            }
        }
    }

    /** Fetch real following list from backend and update [_following]. */
    fun loadFollowing(userId: Int) {
        viewModelScope.launch {
            try {
                val items = userRepository.getFollowing(userId)
                _following.value = items.map { item ->
                    User(
                        id    = item.userID.toString(),
                        name  = item.firstName,
                        userName = item.lastName,
                        image = item.profileImage ?: emptyProfileImage
                    )
                }
            } catch (e: Exception) {
                Log.e("UserVM", "loadFollowing: ${e.message}")
            }
        }
    }

    fun getUserDetails(userId : String) : User?{
        return _users.value.find { it.id == userId }
    }

    fun editUser(
        id: String,
        name: String,
        userName: String,
        bio: String,
        imageUri: Uri?,                // LOCAL Uri picked from gallery
        existingImageUrl: String?,     // Keep the old URL if no new image picked
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                // 1. Upload new profile image if chosen
                val finalImageUrl: String? = if (imageUri != null) {
                    val media = mediaRepository.uploadImage(imageUri)
                    media.filePath.also { Log.d("UserVM", "Profile image uploaded: $it") }
                } else {
                    existingImageUrl ?: emptyProfileImage
                }

                // 2. Send profile update
                val request = UpdateUserRequest(
                    firstName    = name,
                    lastName     = userName,
                    bio          = bio,
                    profileImage = finalImageUrl
                )
                userRepository.editUser(id, request)

                // 3. Update local state so UI reflects the change immediately
                _currentUser.value = _currentUser.value.copy(
                    name        = name,
                    userName    = userName,
                    description = bio,
                    image       = finalImageUrl ?: emptyProfileImage
                )

                onSuccess()
                Log.d("UserVM", "editUser OK")
            } catch (e: Exception) {
                Log.e("UserVM", "editUser: ${e.message}")
            } finally {
                _isLoading.value = false
                onSuccess()
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
                _users.value =_users.value.map {
                    if (it.id == userId) {
                        if (it.isFollowedByMe) it.copy(isFollowedByMe = false)

                        else it.copy(isFollowedByMe = true)
                    }
                    else it
                }

                val response = userRepository.unfollowUser(userId)
                Log.d("API", "unfollowUser success: $response")
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