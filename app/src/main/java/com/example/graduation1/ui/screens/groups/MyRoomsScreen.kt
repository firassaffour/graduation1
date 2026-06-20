package com.example.graduation1.ui.screens.groups

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.graduation1.R
import com.example.graduation1.domain.models.AppPages
import com.example.graduation1.ui.components.GroupUI
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.ui.theme.gray
import com.example.graduation1.ui.theme.primaryRed
import com.example.graduation1.viewmodel.GroupsViewModel
import com.example.graduation1.viewmodel.UserViewModel


@Composable
fun MyRoomsScreen(navController: NavHostController, groupsViewModel: GroupsViewModel, userViewModel: UserViewModel){

    val groupsList by groupsViewModel.groups.collectAsState()
    val newGroupId by groupsViewModel.newGroupId.collectAsState()
    
    val roomsCount by remember { mutableStateOf(groupsList.size.toString()) }
    Column(modifier = Modifier
        .fillMaxSize()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
        }

        Spacer(Modifier.height(10.dp))

        Text(text = stringResource(R.string.MyRooms),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(start = 40.dp))

        Spacer(Modifier.height(10.dp))

        Text(text = "${stringResource(R.string.Youhave)} $roomsCount ${stringResource(R.string.Rooms)}",
            color = gray,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 40.dp))

        Spacer(Modifier.weight(1f))


        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
            items(groupsList, key = {it.id}){ room ->
                GroupUI(navController, room, room.id == newGroupId, groupsViewModel, userViewModel)
            } // items
        } // LazyVerticalGrid

        Spacer(Modifier.weight(1f))

        FloatingActionButton(
            onClick = {},
            containerColor = Color(253, 33, 71, 255),
            shape = CircleShape,
            modifier = Modifier
                .wrapContentWidth()
                .height(60.dp)
                .padding(8.dp)
                .offset(x = (-10).dp, y = (-10).dp)
                .align(Alignment.End)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.addbutton),
                contentDescription = "add",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(30.dp)
                    .clickable { navController.navigate(AppPages.CreateGroup.route) }
            )
        }
    } // Column
}

@Composable
@Preview(showBackground = true)
fun MyRoomsScreenPreview(){
    Graduation1Theme(dynamicColor = true) {
    }
}