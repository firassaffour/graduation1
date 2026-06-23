package com.example.graduation1.ui.screens.groups

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.graduation1.R
import com.example.graduation1.domain.models.AppPages
import com.example.graduation1.domain.models.BottomNavItem
import com.example.graduation1.domain.models.PostData
import com.example.graduation1.selectedPostPage
import com.example.graduation1.ui.components.PostUI
import com.example.graduation1.ui.screens.home.CommentsScreen
import com.example.graduation1.ui.theme.darkGray
import com.example.graduation1.viewmodel.GroupsViewModel
import com.example.graduation1.viewmodel.PostViewModel
import com.example.graduation1.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailsScreen(navController: NavHostController, groupId : String, groupsViewModel: GroupsViewModel, postViewModel: PostViewModel, userViewModel: UserViewModel){

    val groupsList by groupsViewModel.groups.collectAsState()
    val group = groupsList.find { it.id == groupId } ?: return

    val postsList by postViewModel.posts.collectAsState()
    val groupPosts = postsList.filter { it.groupId == groupId }
    val newPostId by postViewModel.newPostId.collectAsState()

    val usersList by userViewModel.users.collectAsState()
    val groupMembers = usersList.filter { it.groupsList.contains(groupId) }
    val currentUser by userViewModel.currentUser.collectAsState()

    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedPost by remember { mutableStateOf<PostData?>(null) }

    val isMember = groupsViewModel.getMemberShip(groupId)

    Column(modifier = Modifier
        .fillMaxSize()) {

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
        } // Row

        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)) {
            item {
                Image(
                    rememberAsyncImagePainter(group.image),
                    contentDescription = "group image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )

                Spacer(Modifier.width(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(Modifier.padding(start = 16.dp)) {
                        Text(
                            text = group.name,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "${groupMembers.count()} ${stringResource(R.string.Members)}",
                            color = darkGray,
                            fontSize = 14.sp
                        )
                    }
                } // Row

                Spacer(Modifier.width(10.dp))

                Button(
                    onClick = {
                        if (isMember) groupsViewModel.leaveGroup(groupId)
                        else groupsViewModel.joinGroup(groupId)
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isMember) darkGray
                            else MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    Text(
                        text = if (isMember) stringResource(R.string.Joined)
                        else stringResource(R.string.Join),
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
                Spacer(Modifier.height(20.dp))
            } // item

            items(groupPosts,
                key = {it.postId}) { post ->
                PostUI(navController, post, post.postId == newPostId, postViewModel, userViewModel, groupsViewModel,
                    onPostClicked = {
                        selectedPostPage = post
                        navController.navigate("${AppPages.Post.route}/${post.postId}")},
                    onCommentClicked = {
                        selectedPost = post
                        showBottomSheet = true
                    },
                    onGroupClicked = {
                    },
                    onPostDeleted = {})
            } // items
        } // LazyColumn
    } // Column
    if (showBottomSheet && selectedPost != null){
        ModalBottomSheet(
            onDismissRequest = {showBottomSheet = false},
            containerColor = MaterialTheme.colorScheme.background
        ) {
            CommentsScreen(navController, selectedPost!!.postId, postViewModel, userViewModel)
        } // ModalBottomSheet
    } // if
}