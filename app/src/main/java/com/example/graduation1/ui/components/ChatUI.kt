package com.example.graduation1.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.graduation1.R
import com.example.graduation1.domain.models.AppPages
import com.example.graduation1.domain.models.ChatItem
import com.example.graduation1.emptyProfileImage
import com.example.graduation1.ui.theme.darkGray
import com.example.graduation1.ui.theme.darkGreen
import com.example.graduation1.ui.theme.primaryRed
import com.example.graduation1.user
import com.example.graduation1.viewmodel.ChatViewModel
import com.example.graduation1.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatUI(navController : NavHostController, chat: ChatItem, chatViewModel : ChatViewModel, userViewModel: UserViewModel){
    val userList by chatViewModel.allUsers.collectAsState()
    val user = userList.find { it.id == chat.userId } ?: user
    val isSeenCount = chatViewModel.getUnSeenMessagesCount(chat.userId)
    val lastMessage = chat.messagesList.lastOrNull()


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp)
            .clickable { navController.navigate("${AppPages.Messaging.route}/${chat.chatId}") }
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate("${AppPages.Messaging.route}/${chat.chatId}") },
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box {
                Image(
                    if (user.image == "") rememberAsyncImagePainter(emptyProfileImage)
                    else rememberAsyncImagePainter(user.image),
                    contentDescription = "image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(shape = CircleShape)
                )

                if (user.isOnline) {
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
                    .weight(5f)
                    .padding(8.dp)
            ) {
                Text(
                    text = user.name,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = if (lastMessage!!.image != null && lastMessage.text.isEmpty()) stringResource(R.string.Image) ?: ""
                        else lastMessage.text ?: "",
                    color = darkGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            } // Column

            Column(modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = chatViewModel.getMessageTime(lastMessage!!.createdAt),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                Spacer(Modifier.height(10.dp))

                /*if (isSeenCount != 0) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(primaryRed),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = if (isSeenCount >= 100) "+99"
                            else isSeenCount.toString(),
                            color = MaterialTheme.colorScheme.background,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 1.sp
                        )
                    }
                } // if*/
            } // Column
        } // Row
    } // Column
}