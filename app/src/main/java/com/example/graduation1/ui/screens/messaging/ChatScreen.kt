package com.example.graduation1.ui.screens.messaging

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.graduation1.ui.components.ChatUI
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.viewmodel.ChatViewModel
import com.example.graduation1.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatScreen(navController: NavHostController, chatViewModel: ChatViewModel, userViewModel: UserViewModel){

    val chatsList by chatViewModel.chatsList.collectAsState()
    val chatSearchQuery by chatViewModel.chatSearchQuery.collectAsState()

    val userList by userViewModel.users.collectAsState()
    val userMap = remember(userList) { userList.associateBy { it.id } }

    val filteredChatList = remember (chatSearchQuery, chatsList, userMap) {
        if (chatSearchQuery.isBlank()) chatsList
        else {
            chatsList.filter { chat ->
                userMap[chat.userId]?.name?.contains(chatSearchQuery, ignoreCase = true) == true }
        }
    }


    Column(modifier = Modifier
        .fillMaxSize()) {

        LazyColumn(modifier = Modifier
            .fillMaxWidth()) {
            items(filteredChatList, key = {it.chatId}){ chat ->
               ChatUI(navController, chat, chatViewModel, userViewModel)
            } // items
        } // LazyColumn
    } // Column
}

@Composable
@Preview(showBackground = true)
fun ChatScreenPreview(){
    Graduation1Theme(dynamicColor = false) {
    }
}