package com.example.graduation1.ui.screens.messaging

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.graduation1.ui.components.ChatUI
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.viewmodel.ChatViewModel
import com.example.graduation1.viewmodel.UserViewModel

@Composable
fun ChatScreen(navController: NavHostController, chatViewModel: ChatViewModel, userViewModel: UserViewModel){

    val chatsList by chatViewModel.chatsList.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()) {

        LazyColumn(modifier = Modifier
            .fillMaxWidth()) {
            items(chatsList, key = {it.chatId}){ chat ->
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