package com.example.graduation1.ui.screens.notification

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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.graduation1.R
import com.example.graduation1.data.remote.RetrofitInstance
import com.example.graduation1.data.repository.NotificationRepository
import com.example.graduation1.ui.components.NotificationUI
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.viewmodel.NotificationViewModel
import com.example.graduation1.viewmodel.NotificationViewModelFactory


@Composable
fun NotificationScreen(navController: NavHostController){

    val viewModel : NotificationViewModel = viewModel(
        factory = NotificationViewModelFactory(NotificationRepository(RetrofitInstance.api))
    )

    val todayNotifications by viewModel.todayNotifications.collectAsState()
    val lastWeekNotification by viewModel.lastWeekNotifications.collectAsState()

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

            items(todayNotifications){ notification ->
                NotificationUI(notification)
            } // items

            item{
                Text(
                    text = stringResource(R.string.ThisWeek),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(20.dp)
                )
            }

            items(lastWeekNotification){ notification ->
                NotificationUI(notification)
            } // items
        } // LazyColumn
    } // Column
}

@Composable
@Preview(showBackground = true)
fun NotificationScreenPreview(){
    Graduation1Theme(dynamicColor = false) {
        val nav = rememberNavController()
        NotificationScreen(nav)
    }
}