package com.example.graduation1.ui.components

import android.os.Build
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.graduation1.R
import com.example.graduation1.domain.models.Notification
import com.example.graduation1.friendsList
import com.example.graduation1.ui.theme.brown
import com.example.graduation1.ui.theme.darkGray
import com.example.graduation1.ui.theme.gray
import com.example.graduation1.viewmodel.GroupsViewModel
import com.example.graduation1.viewmodel.NotificationViewModel
import com.example.graduation1.viewmodel.PostViewModel
import com.example.graduation1.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationUI(notification: Notification, notificationViewModel: NotificationViewModel, groupsViewModel: GroupsViewModel, postViewModel: PostViewModel, userViewModel: UserViewModel){

    val groupsList by groupsViewModel.groups.collectAsState()
    val group = groupsList.find { it.id == notification.groupId } ?: return

    val userList by userViewModel.users.collectAsState()
    val groupMembersInformation = userList.filter { it in group.members }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp)) {

        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {

            Image(
                rememberAsyncImagePainter(group.image),
                contentDescription = "image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .clip(shape = CircleShape))

            Column(modifier = Modifier
                .padding(8.dp)) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = group.name,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.weight(1f))

                    Text(
                        text = postViewModel.getTimeAgo(notification.createdAt),
                        color = darkGray,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Text(
                    text = "${group.members.count()} ${stringResource(R.string.Members)}",
                    color = gray,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(modifier = Modifier
                    .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically) {

                    CircledImagesRow(groupMembersInformation.take(4).map { it.image })

                    Spacer(Modifier.weight(1f))

                    Button(onClick = {
                        groupsViewModel.joinGroup(notification.groupId)
                        notificationViewModel.removeNotification(notification.notificationId)
                    },
                        shape = RoundedCornerShape(17.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .wrapContentWidth()
                            .height(30.dp)) {
                        Text(text = stringResource(R.string.Join),
                            color = MaterialTheme.colorScheme.background,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 2.sp)
                    }

                    Spacer(Modifier.width(5.dp))

                    Button(onClick = {notificationViewModel.removeNotification(notification.notificationId)},
                        shape = RoundedCornerShape(17.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = brown
                        ),
                        modifier = Modifier
                            .wrapContentWidth()
                            .height(30.dp)) {
                        Text(text = stringResource(R.string.Decline),
                            color = MaterialTheme.colorScheme.background,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 2.sp)
                    }
                } // Row
            } // Column
        } // Row
    } // Column
}