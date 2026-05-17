package com.example.graduation1.ui.screens.messaging

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.graduation1.chatList
import com.example.graduation1.data.remote.RetrofitInstance
import com.example.graduation1.data.repository.ChatRepository
import com.example.graduation1.domain.models.AppPages
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.ui.theme.darkGray
import com.example.graduation1.ui.theme.darkGreen
import com.example.graduation1.ui.theme.primaryRed
import com.example.graduation1.viewmodel.ChatViewModel
import com.example.graduation1.viewmodel.ChatViewModelFactory

@Composable
fun ChatScreen(navController: NavHostController){

    val viewModel : ChatViewModel = viewModel(
        factory = ChatViewModelFactory(ChatRepository(RetrofitInstance.api))
    )

    val chatsList by viewModel.chatsList.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()) {

        LazyColumn(modifier = Modifier
            .fillMaxWidth()) {
            items(chatsList){ chat ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                        .clickable { navController.navigate("${AppPages.Messaging.route}/${chat.id}") }
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate("${AppPages.Messaging.route}/${chat.id}") },
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box {
                            Image(
                                rememberAsyncImagePainter(chat.image),
                                contentDescription = "image",
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(shape = CircleShape)
                            )

                            if (chat.isOnline) {
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .align(Alignment.BottomEnd)
                                        .clip(CircleShape)
                                        .background(darkGreen)
                                )
                            }
                        }

                        Spacer(Modifier.width(10.dp))

                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            Text(
                                text = chat.name,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = chat.lastMessageText,
                                color = darkGray,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        } // Column

                        Spacer(Modifier.weight(1f))

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = chat.lastMessageTime,
                                color = if (chat.unSeenMessagesCount == 0) MaterialTheme.colorScheme.onBackground
                                else primaryRed,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 2.sp
                            )

                            Spacer(Modifier.height(10.dp))

                            if (chat.unSeenMessagesCount != 0) {
                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clip(CircleShape)
                                        .background(primaryRed),
                                    contentAlignment = Alignment.Center
                                ){
                                    Text(
                                        text = if (chat.unSeenMessagesCount >= 100) "+99"
                                            else chat.unSeenMessagesCount.toString(),
                                        color = MaterialTheme.colorScheme.background,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        lineHeight = 1.sp
                                    )
                                }
                            }
                        } // Column
                    } // Row
                } // Column
            } // items
        } // LazyColumn
    } // Column
}

@Composable
@Preview(showBackground = true)
fun ChatScreenPreview(){
    Graduation1Theme(dynamicColor = false) {
        val nav = rememberNavController()
        ChatScreen(nav)
    }
}