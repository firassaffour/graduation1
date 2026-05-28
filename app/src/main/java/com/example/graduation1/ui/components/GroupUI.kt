package com.example.graduation1.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.graduation1.R
import com.example.graduation1.domain.models.AppPages
import com.example.graduation1.domain.models.Group
import com.example.graduation1.ui.theme.gray
import com.example.graduation1.viewmodel.GroupsViewModel
import com.example.graduation1.viewmodel.UserViewModel

@Composable
fun GroupUI(navController: NavHostController, room: Group, groupsViewModel: GroupsViewModel, userViewModel: UserViewModel){

    val groupsList by groupsViewModel.groups.collectAsState()
    val group = groupsList.find { it.id == room.id } ?: return

    val userList by userViewModel.users.collectAsState()
    val groupMembersInformation = userList.filter { it.id in group.members }

    Column(modifier = Modifier
        .width(100.dp)
        .height(160.dp)
        .padding(8.dp)
        .background(MaterialTheme.colorScheme.surface)
        .clickable { navController.navigate("${ AppPages.InRooms.route }/${room.id}") },
        horizontalAlignment = Alignment.CenterHorizontally) {

        Spacer(Modifier.weight(1f))

        Image(
            rememberAsyncImagePainter(room.image),
            contentDescription = "image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(70.dp)
                .clip(shape = CircleShape)
        )

        Text(
            text = room.name,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "${room.members.count()} ${stringResource(R.string.Members)}",
            color = gray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Row(modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically) {
            CircledImagesRow(groupMembersInformation.take(4).map { it.image })
        } // Row

        Spacer(Modifier.weight(1f))
    } // Column
}