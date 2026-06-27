package com.example.graduation1.ui.screens.friends

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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.graduation1.R
import com.example.graduation1.data.remote.RetrofitInstance
import com.example.graduation1.data.repository.UserRepository
import com.example.graduation1.domain.models.AppPages
import com.example.graduation1.emptyProfileImage
import com.example.graduation1.friendsList
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.ui.theme.darkGray
import com.example.graduation1.ui.theme.darkGreen
import com.example.graduation1.viewmodel.NotificationViewModel
import com.example.graduation1.viewmodel.UserViewModel
import com.example.graduation1.viewmodel.UserViewModelFactory

@Composable
fun FriendsScreen(navController: NavHostController, userViewModel: UserViewModel, notificationViewModel: NotificationViewModel){

    val usersList by userViewModel.users.collectAsState()

    val currentUser by userViewModel.currentUser.collectAsState()

    val isLoading by userViewModel.isLoading.collectAsState()

    val friendsList = usersList.filter { it.id != currentUser.id }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            text = stringResource(R.string.Friends),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(15.dp)
        )

        if (isLoading){
            Spacer(Modifier.height(300.dp))
            CircularProgressIndicator()
        }
        
        else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                items(friendsList) { friend ->
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
                                    if (friend.image == "") rememberAsyncImagePainter(
                                        emptyProfileImage
                                    )
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

                            Button(
                                onClick = {
                                    if (friend.isFollowedByMe) userViewModel.unfollowUser(friend.id)
                                    else {
                                        userViewModel.followUser(friend.id)
                                        notificationViewModel.sendNotification(
                                            userId  = friend.id.toInt(),
                                            type    = "follow",
                                            message = "${currentUser.name} started following you"
                                        )
                                    }
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (friend.isFollowedByMe) darkGray
                                    else MaterialTheme.colorScheme.primary
                                ),
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .height(33.dp)
                            ) {
                                Text(
                                    text = if (friend.isFollowedByMe) stringResource(R.string.UnFollow)
                                    else stringResource(R.string.Follow),
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 2.sp
                                )
                            }
                        } // Row
                    } // Column
                } // items
            } // LazyColumn
        }
    } // Column
}

@Composable
@Preview(showBackground = true)
fun FriendsScreenPreview(){

}