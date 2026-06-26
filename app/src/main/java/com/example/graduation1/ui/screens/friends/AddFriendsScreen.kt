package com.example.graduation1.ui.screens.friends

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.graduation1.R
import com.example.graduation1.domain.models.AppPages
import com.example.graduation1.emptyProfileImage
import com.example.graduation1.ui.theme.darkGray
import com.example.graduation1.ui.theme.darkGreen
import com.example.graduation1.viewmodel.GroupsViewModel
import com.example.graduation1.viewmodel.NotificationViewModel
import com.example.graduation1.viewmodel.UserViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddFriendsScreen(navController: NavHostController, userViewModel: UserViewModel, groupsViewModel: GroupsViewModel, notificationViewModel: NotificationViewModel){

    val groupList by groupsViewModel.groups.collectAsState()
    val groupId by groupsViewModel.currentGroupId.collectAsState()
    val group = groupList.find { it.id ==  groupId} ?: return

    val usersList by userViewModel.users.collectAsState()
    val currentUser by userViewModel.currentUser.collectAsState()

    val notificationsList by notificationViewModel.notifications.collectAsState()

    val friendsList = usersList.filter { !group.members.contains(it)}

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "back",
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(Modifier.weight(1f))

            Text(
                text = stringResource(R.string.Friends),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.weight(1.5f))
        } // Row

        Spacer(Modifier.height(20.dp))

        LazyColumn(modifier = Modifier
            .fillMaxWidth()) {

            items(friendsList) { friend ->

                val notificationIsSent = notificationsList.any { it.userID == friend.id.toInt() && it.type == groupId }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate("${AppPages.OtherUsersProfile.route}/${friend.id}") },
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box() {
                            Image(
                                if (friend.image == "") rememberAsyncImagePainter(emptyProfileImage)
                                else rememberAsyncImagePainter(friend.image),
                                contentDescription = "image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(shape = CircleShape)
                            )

                            if (friend.isOnline) {
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

                        Text(
                            modifier = Modifier
                                .weight(5f)
                                .padding(start = 8.dp),
                            text = friend.name,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(Modifier.weight(1f))

                        Button(onClick = {notificationViewModel.sendNotification(friend.id, groupId)},
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (notificationIsSent) darkGray
                                else MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier
                                .wrapContentWidth()
                                .height(33.dp)) {
                            Text(text = if (notificationIsSent) stringResource(R.string.Sent)
                                else stringResource(R.string.AddFriend),
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 2.sp)
                        }
                    } // Row
                } // Column
            } // items
        } // LazyColumn
    } // Column
}