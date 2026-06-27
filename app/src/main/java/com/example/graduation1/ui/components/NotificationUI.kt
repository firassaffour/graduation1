package com.example.graduation1.ui.components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.graduation1.R
import com.example.graduation1.domain.models.Notification
import com.example.graduation1.domain.models.requets_response.NotificationResponse
import com.example.graduation1.friendsList
import com.example.graduation1.ui.theme.brown
import com.example.graduation1.ui.theme.darkGray
import com.example.graduation1.ui.theme.gray
import com.example.graduation1.user
import com.example.graduation1.viewmodel.GroupsViewModel
import com.example.graduation1.viewmodel.NotificationViewModel
import com.example.graduation1.viewmodel.PostViewModel
import com.example.graduation1.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationUI(notification: NotificationResponse, notificationViewModel: NotificationViewModel, groupsViewModel: GroupsViewModel, postViewModel: PostViewModel, userViewModel: UserViewModel){

    val groupsList by groupsViewModel.groups.collectAsState()

    val userList by userViewModel.users.collectAsState()
    val user = userList.find { it.id.toInt() == notification.userID } ?: user
    val sender = userList.find { notification.message!!.contains(it.name) } ?: user
    Log.d("UI", "NotificationUI: $sender")

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp)) {

        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {

            Image(
                rememberAsyncImagePainter(sender.image),
                contentDescription = "image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .clip(shape = CircleShape))

            Column(modifier = Modifier
                .padding(8.dp)) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = notification.message.toString(),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(modifier = Modifier
                    .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        text = notificationViewModel.getTimeAgo(notification.createdAt),
                        color = darkGray,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(Modifier.weight(1f))

                    IconButton(onClick = {notificationViewModel.deleteNotification(notification.notificationID)}) {
                            Icon(
                                painter            = painterResource(R.drawable.trash),
                                contentDescription = "trash",
                                tint               = MaterialTheme.colorScheme.onBackground,
                                modifier           = Modifier.size(24.dp)
                            )
                        }
                } // Row
            } // Column
        } // Row
    } // Column
}