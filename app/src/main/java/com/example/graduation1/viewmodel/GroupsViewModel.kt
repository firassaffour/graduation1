package com.example.graduation1.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduation1.data.repository.GroupsRepository
import com.example.graduation1.domain.models.Group
import com.example.graduation1.domain.models.User
import com.example.graduation1.groupsList
import com.patrykandpatrick.vico.core.extension.mutableListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GroupsViewModel(private val repository: GroupsRepository) : ViewModel() {

    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups = _groups.asStateFlow()

    private val _members = MutableStateFlow<List<User>>(emptyList())
    val members = _members.asStateFlow()

    private val _currentUserGroups = MutableStateFlow<List<Group>>(emptyList())
    val currentUserGroups = _currentUserGroups.asStateFlow()

    private val _selectedGroup = MutableStateFlow<Group?>(null)
    val selectedGroup = _selectedGroup.asStateFlow()

    init {
        getGroups()
    }

    private fun getGroups(){
        viewModelScope.launch {
            try {
                _groups.value = groupsList
            }
            catch (e: Exception){
                Log.e("API", "GroupsViewModel: ${e.message}")
            }
        }
    }

    fun getUserGroups(userGroups : List<String>){
        viewModelScope.launch {
            try {
                _currentUserGroups.value = _groups.value.filter { it.id in userGroups }
            }
            catch (e: Exception){
                Log.e("API", "GroupsViewModel: ${e.message}")
            }
        }
    }

    fun getGroupMembers(groupId : String){
        viewModelScope.launch {
            try {
                _groups.value.map {
                    if (it.id == groupId)
                        _members.value = it.members
                }
            }
            catch (e: Exception){
                Log.e("API", "GroupsViewModel: ${e.message}")
            }
        }
    }

    fun getOnlineMembers(groupId: String) : Int {
        val onlineMemberCount = mutableIntStateOf(0)
        viewModelScope.launch {
            try {
                _groups.value.map {
                    if (it.id == groupId)
                        it.members.forEach {
                            if (it.isOnline) onlineMemberCount.intValue += 1
                        }
                }
            }
            catch (e: Exception){
                Log.e("API", "GroupsViewModel: ${e.message}")
            }
        }
        return onlineMemberCount.intValue
    }

    fun getFriendsInGroup(group: Group, currentUserFollowingList : List<String>) : Int {
        return group.members.count {
            it.id in currentUserFollowingList
        }
    }

    fun updateSelectedGroup(group: Group){
        _selectedGroup.value = group
    }
}