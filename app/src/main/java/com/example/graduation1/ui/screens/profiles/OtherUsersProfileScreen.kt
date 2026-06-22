package com.example.graduation1.ui.screens.profiles

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.graduation1.R
import com.example.graduation1.data.remote.RetrofitInstance
import com.example.graduation1.data.repository.PostRepository
import com.example.graduation1.data.repository.UserRepository
import com.example.graduation1.domain.models.AppPages
import com.example.graduation1.emptyProfileImage
import com.example.graduation1.groupsList
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.ui.theme.darkGray
import com.example.graduation1.ui.theme.darkGreen
import com.example.graduation1.ui.theme.gradientGray
import com.example.graduation1.ui.theme.gray
import com.example.graduation1.user
import com.example.graduation1.viewmodel.GroupsViewModel
import com.example.graduation1.viewmodel.PostViewModel
import com.example.graduation1.viewmodel.PostViewModelFactory
import com.example.graduation1.viewmodel.UserViewModel
import com.example.graduation1.viewmodel.UserViewModelFactory

@Composable
fun OtherUsersProfileScreen(navController: NavHostController, userId : String, userViewModel: UserViewModel, postViewModel: PostViewModel, groupsViewModel: GroupsViewModel){

    val usersList by userViewModel.users.collectAsState()
    val user = usersList.first { it.id == userId }

    val currentUser by userViewModel.currentUser.collectAsState()

    LaunchedEffect(Unit) {
        groupsViewModel.getUserGroups(user.groupsList)
    }

    val groupList by groupsViewModel.currentUserGroups.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Box(modifier = Modifier
            .width(412.dp)
            .height(226.dp)
            .clip(
                GenericShape{ size, _ ->
                    moveTo(0f, 0f) // top left
                    lineTo(size.width, 0f) // top right
                    lineTo(size.width, size.height * 0.6f) // bottom right
                    lineTo(0f, size.height) // bottom left
                    close()
                }
            )
            .background(gradientGray),
            contentAlignment = Alignment.BottomCenter){

            IconButton(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopStart),
                onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "back",
                    modifier = Modifier
                        .size(30.dp)

                )
            }

        } // Box
        Box(modifier = Modifier
            .offset(y = (-120).dp)
            .size(140.dp),
            contentAlignment = Alignment.Center){
            Image(
                if (user.image == "") rememberAsyncImagePainter(emptyProfileImage)
                else rememberAsyncImagePainter(user.image),
                contentDescription = "image",
                modifier = Modifier
                    .size(140.dp)
                    .clip(shape = CircleShape)
                    .border(4.dp, Color.Black, CircleShape)
            )

            if (user.isOnline) {
                Box(
                    modifier = Modifier
                        .offset(x = (-8).dp, y = (-8).dp)
                        .size(25.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(darkGreen)
                )
            }
        }

        Text(
            text = user.name,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .offset(y = -(100).dp)
        )

        Text(
            text = user.description,
            color = gray,
            fontSize = 21.sp,
            modifier = Modifier
                .offset(y = -(100).dp)
        )

        Row(modifier = Modifier.offset(y = -(90).dp),
            verticalAlignment = Alignment.CenterVertically) {

            Icon(
                painter = painterResource(id = R.drawable.location),
                contentDescription = "location",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(30.dp)
            )

            Text(
                text = user.location,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 21.sp
            )
        } // Row

        Row(modifier = Modifier.offset(y = -(70).dp),
            verticalAlignment = Alignment.CenterVertically) {

            Spacer(Modifier.weight(5f))

            Button(onClick = {userViewModel.followUser(user.id)},
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (user.followersList.contains(currentUser.id)) darkGray
                        else MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .wrapContentWidth()
                    .height(40.dp)) {
                Text(text = if (user.followersList.contains(currentUser.id)) stringResource(R.string.UnFollow)
                else stringResource(R.string.Follow),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 2.sp)
            }

            Spacer(Modifier.weight(2f))

            Button(onClick = {navController.navigate("${AppPages.Messaging.route}/${user.id}")},
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = darkGray
                ),
                modifier = Modifier
                    .wrapContentWidth()
                    .height(40.dp)) {
                Text(text = stringResource(R.string.Message),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 2.sp)
            }

            Spacer(Modifier.weight(2f))

            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(id = R.drawable.share),
                    contentDescription = "share",
                    modifier = Modifier.size(30.dp)
                )
            }
            Spacer(Modifier.weight(1f))
        } // Row

        Row(modifier = Modifier.offset(y = -(40).dp),
            verticalAlignment = Alignment.CenterVertically) {

            Spacer(Modifier.weight(1f))

            Card(modifier = Modifier
                .wrapContentWidth()
                .height(76.dp),
                border = BorderStroke(width = 4.dp, color = MaterialTheme.colorScheme.onBackground),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            ) {
                Column(modifier = Modifier
                    .width(100.dp)
                    .padding(top = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        text = postViewModel.getPostsCount(userId).toString(),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = stringResource(R.string.Post),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp
                    )
                } // Column
            } // Card

            Spacer(Modifier.weight(1f))

            Card(modifier = Modifier
                .wrapContentWidth()
                .height(76.dp),
                border = BorderStroke(width = 4.dp, color = MaterialTheme.colorScheme.onBackground),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            ) {
                Column(modifier = Modifier
                    .width(100.dp)
                    .padding(top = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        text = user.followersList.count().toString(),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = stringResource(R.string.Followers),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp
                    )
                } // Column
            } // Card

            Spacer(Modifier.weight(1f))

            Card(modifier = Modifier
                .wrapContentWidth()
                .height(76.dp),
                border = BorderStroke(width = 4.dp, color = MaterialTheme.colorScheme.onBackground),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            ) {
                Column(modifier = Modifier
                    .width(100.dp)
                    .padding(top = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        text = user.followingList.count().toString(),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = stringResource(R.string.Following),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp
                    )
                } // Column
            } // Card

            Spacer(Modifier.weight(1f))
        } // Row

        Row(modifier = Modifier
            .offset(y = -(20).dp)
            .fillMaxWidth()
            .padding(start = 8.dp),
            verticalAlignment = Alignment.CenterVertically) {

            Icon(
                painter = painterResource(id = R.drawable.email),
                contentDescription = "email",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(30.dp)
            )

            Spacer(Modifier.width(5.dp))

            Text(
                text = user.email,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp
            )
        } // Row

        Row(modifier = Modifier
            .offset(y = -(10).dp)
            .fillMaxWidth()
            .padding(start = 8.dp),
            verticalAlignment = Alignment.CenterVertically) {

            Icon(
                painter = painterResource(id = R.drawable.birthday),
                contentDescription = "birthday",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(30.dp)
            )

            Spacer(Modifier.width(5.dp))

            Text(
                text = user.birthday,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp
            )
        } // Row

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp),
            verticalAlignment = Alignment.CenterVertically) {

            Icon(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "profile",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(30.dp)
            )

            Spacer(Modifier.width(5.dp))

            Text(
                text = if (user.gender == "Male") stringResource(R.string.Male)
                else stringResource(R.string.Female),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp
            )
        } // Row

        Spacer(Modifier.height(10.dp))

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = stringResource(R.string.Groups),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.weight(1f))
        } // Row

        Spacer(Modifier.height(10.dp))

        LazyRow(modifier = Modifier
            .fillMaxWidth()) {
            items(groupList){ group ->
                Box(modifier = Modifier
                    .size(90.dp)
                    .padding(8.dp)
                    .clip(shape = CircleShape),
                    contentAlignment = Alignment.Center){
                    Image(
                        rememberAsyncImagePainter(group.image),
                        contentDescription = "image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(90.dp)
                            .clip(shape = CircleShape)
                            .clickable { navController.navigate("${AppPages.GroupDetails.route}/${group.id}") }
                    )
                }
            } // items
        } // LazyRow
    } // Column
}

@Composable
@Preview(showBackground = true)
fun OtherUsersProfileScreenPreview(){
    Graduation1Theme(dynamicColor = false) {
        val nav = rememberNavController()
    }
}