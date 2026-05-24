package com.example.graduation1.ui.screens.groups

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.graduation1.R
import com.example.graduation1.domain.models.AppPages
import com.example.graduation1.ui.components.CircledImagesRow
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.ui.theme.darkGreen
import com.example.graduation1.ui.theme.gray
import com.example.graduation1.ui.theme.primaryRed
import com.example.graduation1.viewmodel.GroupsViewModel
import com.example.graduation1.viewmodel.UserViewModel


@Composable
fun InRoomsScreen(navController: NavHostController, groupId : String, groupsViewModel: GroupsViewModel, userViewModel: UserViewModel){


    val groupsList by groupsViewModel.groups.collectAsState()
    val group = groupsList.find { it.id == groupId } ?: return
    val groupMembers by groupsViewModel.members.collectAsState()

    val userList by userViewModel.users.collectAsState()
    val groupMembersInformation = userList.filter { it.id in group.members }

    val onlineMembersCount by remember { mutableStateOf(groupsViewModel.getOnlineMembers(groupMembersInformation).toString()) }

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(shape = CircleShape) {
                IconButton(
                    modifier = Modifier.background(primaryRed),
                    onClick = {}) {
                    Icon(
                        painter = painterResource(id = R.drawable.menu),
                        contentDescription = "menu",
                        tint = MaterialTheme.colorScheme.background,
                        modifier = Modifier
                            .size(25.dp)
                    )
                }
            } // Card

            Spacer(Modifier.weight(1f))

            Card(shape = CircleShape) {
                IconButton(
                    modifier = Modifier.background(primaryRed),
                    onClick = {}) {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = "search",
                        tint = MaterialTheme.colorScheme.background,
                        modifier = Modifier
                            .size(25.dp)
                    )
                }
            } // Card
        } // Row

        Spacer(Modifier.height(10.dp))

        Text(text = stringResource(R.string.InTheRoom),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(start = 30.dp))

        Spacer(Modifier.height(10.dp))

        Row {
            Text(text = "$onlineMembersCount ${stringResource(R.string.PeopleAreAround)}",
                color = gray,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 30.dp))

            Spacer(Modifier.weight(1f))

            Text(text = stringResource(R.string.SeeMore),
                color = Color(71, 155, 160, 255),
                fontSize = 14.sp,
                modifier = Modifier.padding(end = 20.dp))
        } // Row

        Spacer(Modifier.height(10.dp))

        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)) {

            items(groupMembersInformation) { member ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate("${AppPages.OtherUsersProfile.route}/${member.id}") },
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box {

                            Image(
                                rememberAsyncImagePainter(member.image),
                                contentDescription = "image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(shape = CircleShape)
                            )

                            if (member.isOnline) {
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .align(Alignment.BottomEnd)
                                        .clip(CircleShape)
                                        .background(darkGreen)
                                )
                            }
                        }

                        Text(
                                modifier = Modifier
                                    .weight(5f)
                                    .padding(start = 16.dp),
                                text = member.name,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                        Spacer(Modifier.weight(1f))

                        Button(onClick = { navController.navigate("${AppPages.Messaging.route}/${member.id}") },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.background
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
                            modifier = Modifier
                                .wrapContentWidth()
                                .height(37.dp)) {
                            Icon(
                                painter = painterResource(id = R.drawable.chatred),
                                contentDescription = "chat",
                                tint = Color.Unspecified,
                                modifier = Modifier
                                    .size(35.dp)
                            )
                        }
                    } // Row
                } // Column
            } // items
        } // LazyColumn

        Spacer(Modifier.height(20.dp))

        Row {
            Text(text = stringResource(R.string.YourRecentGroups),
                color = gray,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 30.dp))

            Spacer(Modifier.weight(1f))

            Text(text = stringResource(R.string.SeeMore),
                color = Color(71, 155, 160, 255),
                fontSize = 14.sp,
                modifier = Modifier.padding(end = 20.dp))
        } // Row

        Spacer(Modifier.height(10.dp))

        LazyRow(modifier = Modifier
            .fillMaxWidth()) {
            items(groupsList){ group ->

                    Column(modifier = Modifier
                        .width(180.dp)
                        .height(180.dp)
                        .padding(8.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .clickable { navController.navigate("${ AppPages.InRooms.route }/${group.id}") },
                        horizontalAlignment = Alignment.CenterHorizontally) {

                        Spacer(Modifier.height(20.dp))

                        Image(
                            rememberAsyncImagePainter(group.image),
                            contentDescription = "image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(70.dp)
                                .clip(shape = CircleShape)
                        )

                        Text(
                            text = group.name,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "${group.membersCount} ${stringResource(R.string.Members)}",
                            color = gray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Row(modifier = Modifier,
                            verticalAlignment = Alignment.CenterVertically) {
                            CircledImagesRow(groupMembersInformation.take(4).map { it.image })
                        } // Row
                    } // Column
            } // items
        } // LazyRow
    } // Column
}

@Composable
@Preview(showBackground = true)
fun InRoomsScreenPreview(){
    Graduation1Theme(dynamicColor = true) {
    }
}