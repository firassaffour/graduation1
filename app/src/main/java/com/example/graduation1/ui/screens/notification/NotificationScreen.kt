package com.example.graduation1.ui.screens.notification

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.graduation1.R
import com.example.graduation1.ui.components.NotificationUI
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.viewmodel.GroupsViewModel
import com.example.graduation1.viewmodel.NotificationViewModel
import com.example.graduation1.viewmodel.PostViewModel
import com.example.graduation1.viewmodel.UserViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationScreen(navController: NavHostController, notificationViewModel: NotificationViewModel, groupsViewModel: GroupsViewModel, postViewModel: PostViewModel, userViewModel: UserViewModel){

    val todayNotifications by notificationViewModel.todayNotifications.collectAsState()
    val lastWeekNotification by notificationViewModel.lastWeeksNotifications.collectAsState()

    LaunchedEffect(Unit) {
        notificationViewModel.markAllNotificationsRead()
        notificationViewModel.getUnreadCount()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {

            IconButton(onClick = {navController.popBackStack()}) {
                Icon(
                    painter = painterResource(id = R.drawable.rightarrow),
                    contentDescription = "close",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(40.dp)
                        .rotate(180f)
                )
            }
        } // Row

        Text(
            text = stringResource(R.string.Notifications),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold
        )

        LazyColumn(modifier = Modifier
            .fillMaxWidth()) {

            if (todayNotifications.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.Today),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(20.dp)
                    )
                }

                items(todayNotifications) { notification ->
                    NotificationUI(
                        notification,
                        notificationViewModel,
                        groupsViewModel,
                        postViewModel,
                        userViewModel
                    )
                } // items
            }

            if (lastWeekNotification.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.LastWeek),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(20.dp)
                    )
                }

                items(lastWeekNotification) { notification ->
                    NotificationUI(
                        notification,
                        notificationViewModel,
                        groupsViewModel,
                        postViewModel,
                        userViewModel
                    )
                } // items
            }

            if (todayNotifications.isEmpty() && lastWeekNotification.isEmpty()){
                item {
                    Text(
                        text = stringResource(R.string.NoNotification),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(20.dp)
                    )
                }
            }
        } // LazyColumn
    } // Column
}

@Composable
@Preview(showBackground = true)
fun NotificationScreenPreview(){
    Graduation1Theme(dynamicColor = false) {
        val nav = rememberNavController()
    }
}