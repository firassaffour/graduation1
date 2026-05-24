package com.example.graduation1.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.graduation1.R
import com.example.graduation1.domain.models.AppPages
import com.example.graduation1.domain.models.PostData
import com.example.graduation1.selectedPostPage
import com.example.graduation1.ui.components.PostUI
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.ui.theme.primaryRed
import com.example.graduation1.viewmodel.PostViewModel
import com.example.graduation1.viewmodel.UserViewModel


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, postViewModel: PostViewModel, userViewModel: UserViewModel){

    val postsList by postViewModel.posts.collectAsState()
    val newPostId by postViewModel.newPostId.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    val filteredPosts = remember (searchQuery, postsList) {
        if (searchQuery.isBlank()) postsList
        else postsList.filter { it.postText.contains(searchQuery, ignoreCase = true) }
    }

    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedPost by remember { mutableStateOf<PostData?>(null) }

    val listState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }
    LaunchedEffect(newPostId) {
        if (newPostId.isNotEmpty())
            listState.animateScrollToItem(0)
    }

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.logoimage),
                        contentDescription = "logo",
                        modifier = Modifier
                            .size(40.dp)
                    )

                    Spacer(Modifier.width(10.dp))

                    Text(
                        text = "EduConnect",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.weight(1f))

                    IconButton(onClick = { navController.navigate(AppPages.Notification.route) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.notification),
                            contentDescription = "notification",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(25.dp)
                        )
                    }

                    IconButton(onClick = { navController.navigate(AppPages.MyRooms.route) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.communities),
                            contentDescription = "communities",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                } // Row

                Spacer(Modifier.height(10.dp))

                Row {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
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
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.background,
                            unfocusedContainerColor = MaterialTheme.colorScheme.background
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentHeight()
                    )
                } // Row
            } // Column

            Spacer(Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                state = listState
            ) {
                items(
                    filteredPosts,
                    key = {it.postId}) { post ->
                    PostUI(navController, post, post.postId == newPostId, postViewModel, userViewModel,
                        onPostClicked = {
                            selectedPostPage = post
                            navController.navigate("${AppPages.Post.route}/${post.postId}")},
                        onCommentClicked = {
                            selectedPost = post
                            showBottomSheet = true
                        })
                } // items
            } // LazyColumn
            Spacer(Modifier.height(10.dp))
        } // column
        FloatingActionButton(
            onClick = {navController.navigate(AppPages.ChatbotStart.route)},
            containerColor = primaryRed,
            modifier = Modifier
                .wrapContentWidth()
                .height(60.dp)
                .padding(8.dp)
                .align(Alignment.BottomEnd)
        ) {

            Row(modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.chatbot),
                    contentDescription = "message",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(25.dp)
                )

                Text(
                    text = "  ${stringResource(R.string.ChatBot)}",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            } // Row
        } // FloatingActionButton
        if (showBottomSheet && selectedPost != null){
            ModalBottomSheet(
                onDismissRequest = {showBottomSheet = false},
                containerColor = MaterialTheme.colorScheme.background
            ) {
                CommentsScreen(navController, selectedPost!!.postId, postViewModel, userViewModel)
            } // ModalBottomSheet
        } // if
    } // Box
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
fun HomeScreenPreview(){
    Graduation1Theme(dynamicColor = false) {
    }
}