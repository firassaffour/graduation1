package com.example.graduation1.ui.screens.messaging

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.graduation1.R
import com.example.graduation1.ui.screens.groups.GroupsScreen
import com.example.graduation1.ui.theme.primaryRed
import com.example.graduation1.viewmodel.ChatViewModel
import com.example.graduation1.viewmodel.GroupsViewModel
import com.example.graduation1.viewmodel.UserViewModel

@Composable
fun ChatTabs(navController: NavHostController, chatViewModel: ChatViewModel, groupsViewModel: GroupsViewModel, userViewModel: UserViewModel){
    val tabs = listOf(
        stringResource(R.string.Allchats),
        stringResource(R.string.Groups),
        stringResource(R.string.Contacts))
    var selectedTab by rememberSaveable { mutableStateOf(0) }

    val chatSearchQuery by chatViewModel.chatSearchQuery.collectAsState()

    Column(modifier = Modifier.padding(8.dp)) {

        when (selectedTab){
            0 -> {
                Row {
                    OutlinedTextField(
                        value = chatSearchQuery,
                        onValueChange = { chatViewModel.updateChatSearchQuery(it) },
                        placeholder = {
                            Text(
                                stringResource(R.string.Search),
                                maxLines = 1,
                                fontSize = 16.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.search),
                                contentDescription = "search",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .size(20.dp)
                            )
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.background,
                            unfocusedContainerColor = MaterialTheme.colorScheme.background
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentHeight()
                            .padding(16.dp)
                    )
                } // Row
            }
            1 -> {
                Button(onClick = {},
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(40.dp)
                        .align(Alignment.End)) {

                    Text(text = "+",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 2.sp)

                    Spacer(Modifier.width(10.dp))

                    Text(text = "Create Group",
                        color = MaterialTheme.colorScheme.background,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 2.sp)
                }

                Spacer(Modifier.height(40.dp))
            }
            2 -> {
                Button(onClick = {},
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(40.dp)
                        .align(Alignment.End)) {

                    Text(text = "+",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 2.sp)

                    Spacer(Modifier.width(10.dp))

                    Text(text = "Create Group",
                        color = MaterialTheme.colorScheme.background,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 2.sp)
                }

                Spacer(Modifier.height(40.dp))
            }
        }

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.surface)
        ) {
            tabs.forEachIndexed { index, title ->

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (selectedTab == index) primaryRed
                            else MaterialTheme.colorScheme.surface
                        )
                        .clickable { selectedTab = index }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        color = if (selectedTab == index) Color.White else Color.Gray
                    )
                }
            }
        }
        when (selectedTab){
            0 -> ChatScreen(navController, chatViewModel, userViewModel)
            1 -> GroupsScreen(navController)
            2 -> GroupsScreen(navController)
        }
    } // Column
}