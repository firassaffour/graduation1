package com.example.graduation1.viewmodel

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduation1.data.repository.GroupsRepository
import com.example.graduation1.data.repository.UserRepository
import com.example.graduation1.domain.models.Group
import com.example.graduation1.domain.models.PostData
import com.example.graduation1.domain.models.User
import com.example.graduation1.domain.models.requets_response.CreateCommunityRequest
import com.example.graduation1.groupsList
import com.example.graduation1.user
import com.patrykandpatrick.vico.core.extension.mutableListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class GroupsViewModel(private val groupsRepository: GroupsRepository, private val userRepository: UserRepository) : ViewModel() {

    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups = _groups.asStateFlow()

    private val _members = MutableStateFlow<List<User>>(emptyList())
    val members = _members.asStateFlow()

    private val _currentUserGroups = MutableStateFlow<List<Group>>(emptyList())
    val currentUserGroups = _currentUserGroups.asStateFlow()

    private val _selectedGroup = MutableStateFlow<Group?>(null)
    val selectedGroup = _selectedGroup.asStateFlow()

    private val _groupName = MutableStateFlow("")
    val groupName  = _groupName.asStateFlow()

    private val _currentUser = MutableStateFlow<User>(user)
    val currentUser  = _currentUser.asStateFlow()

    private val _newGroupId = MutableStateFlow("")
    val newGroupId = _newGroupId.asStateFlow()

    private val _currentGroupId = MutableStateFlow("")
    val currentGroupId = _currentGroupId.asStateFlow()

    init {
        getGroups()
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

    private fun getGroups(){
        viewModelScope.launch {
            try {
                _groups.value = groupsRepository.getCommunities().map {
                    it.copy(members = groupsRepository.getCommunityMembers(it.id.toInt()),
                        isMember = groupsRepository.getCommunityMembership(it.id.toInt()).isMember)
                }
            }
            catch (e: Exception){
                Log.e("API", "GroupsViewModel: ${e.message}")
            }
        }
    }

    fun getUserGroups(userGroups : List<String>){
        viewModelScope.launch {
            try {
                _currentUserGroups.value = _groups.value
            }
            catch (e: Exception){
                Log.e("API", "GroupsViewModel: ${e.message}")
            }
        }
    }

    fun getMembers(groupId: String) : List<User>{
        var response = emptyList<User>()
        viewModelScope.launch {
            try {
                response = groupsRepository.getCommunityMembers(groupId.toInt())
                Log.d("API", "Group member success: $response")
            }
            catch (e : Exception){
                Log.e("API", "member Group error: ${e.message}", )
            }
        }
        return response
    }

    fun getMemberShip(groupId: String) : Boolean{
        var response = false
        viewModelScope.launch {
            try {
                response = groupsRepository.getCommunityMembership(groupId.toInt()).isMember
                Log.d("API", "Group membership success: $response")
            }
            catch (e : Exception){
                Log.e("API", "Group membership error: ${e.message}", )
            }
        }
        return response
    }


    fun getOnlineMembers( membersList : List<User>) : Int {
        return membersList.filter { it.isOnline }.size
    }

    fun getFriendsInGroup(group: Group, currentUserFollowingList : List<User>) : Int {
        return group.members.count {
            it in currentUserFollowingList
        }
    }

    fun updateSelectedGroup(group: Group?){
        _selectedGroup.value = group
    }

    fun updateGroupName(groupName : String){
        _groupName.value = groupName
    }

    fun updateNewGroupId(id : String){
        _newGroupId.value = id
    }

    fun updateCurrentGroupId(id : String){
        _currentGroupId.value = id
    }

    @SuppressLint("SuspiciousIndentation")
    fun joinGroup(groupId : String){
        if (_currentUser.value.groupsList.contains(groupId)) _currentUser.value.copy(groupsList = _currentUser.value.groupsList - groupId)

            else _currentUser.value = _currentUser.value.copy(groupsList = _currentUser.value.groupsList + groupId)

            _groups.value = _groups.value.map {
                if (it.id == groupId){
                    it.copy(members =
                        if (it.isMember) it.members + _currentUser.value
                        else it.members - _currentUser.value,
                        isMember = if (it.isMember) false
                        else true)
                }

                else it
            }

        viewModelScope.launch {
            try {
                val response = groupsRepository.joinCommunity(groupId.toInt())
                Log.d("API", "joinGroup success: $response")
            }
            catch (e : Exception){
                Log.e("API", "joinGroup error: ${e.message}", )
            }
        }
    }

    fun leaveGroup(groupId: String){
        viewModelScope.launch {
            try {
                val response = groupsRepository.leaveCommunity(groupId.toInt())
                Log.d("API", "leaveGroup success: $response")
            }
            catch (e : Exception){
                Log.e("API", "leaveGroup error: ${e.message}", )
            }
        }
    }


    @OptIn(ExperimentalUuidApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    fun createGroup(name : String, image : String ){
        viewModelScope.launch {
            try {
                val group = Group(
                    Uuid.random().toString(),
                    currentUser.value.id,
                    name,
                    image,
                    listOf(currentUser.value)
                )
                _groups.value = listOf(group) + _groups.value
                _newGroupId.value = group.id

                _currentUser.value = _currentUser.value.copy(groupsList = _currentUser.value.groupsList + group.id)

                val groupData = CreateCommunityRequest(
                    name,
                    ""
                )

                val response = groupsRepository.createCommunity(groupData)
                Log.d("API", "createGroup success: $groupData")

            }
            catch (e: Exception){
                Log.e("API", "groupViewModel create group: ${e.message}")
            }
        }
    }
}