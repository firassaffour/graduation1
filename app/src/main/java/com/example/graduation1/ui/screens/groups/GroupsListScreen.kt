package com.example.graduation1.ui.screens.groups

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.graduation1.R
import com.example.graduation1.ui.components.CircledImagesRow
import com.example.graduation1.viewmodel.GroupsViewModel
import com.example.graduation1.viewmodel.UserViewModel

@Composable
fun GroupsListScreen(navController: NavHostController, userId : String, groupsViewModel: GroupsViewModel, userViewModel : UserViewModel, onGroupClicked : () -> Unit){

    val user = userViewModel.getUserDetails(userId)
    LaunchedEffect(Unit) {
        groupsViewModel.getUserGroups(user.groupsList)
    }

    val groupsList by groupsViewModel.currentUserGroups.collectAsState()

    val userList by userViewModel.users.collectAsState()



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
                text = stringResource(R.string.Groups),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.weight(1.5f))
        } // Row

        LazyColumn(modifier = Modifier
            .fillMaxWidth()) {

            items(groupsList){ group ->
                val groupMembersInformation = userList.filter { it.id in group.members }
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
                    .clickable {
                        groupsViewModel.updateSelectedGroup(group)
                        navController.popBackStack()
                    }) {

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
                            Text(
                                text = group.name,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "${groupsViewModel.getFriendsInGroup(group, user.followingList)} ${stringResource(R.string.Friends)}   ${group.membersCount} ${stringResource(R.string.Members)}",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Row(modifier = Modifier
                                .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically) {

                                CircledImagesRow(groupMembersInformation.take(4).map { it.image })

                                Spacer(Modifier.weight(1f))
                            } // Row
                        } // Column
                    } // Row
                } // Column
            } // items
        } // LazyColumn
    } // Column
}