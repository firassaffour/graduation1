package com.example.graduation1.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.graduation1.R
import com.example.graduation1.darkMode
import com.example.graduation1.data.remote.RetrofitInstance
import com.example.graduation1.data.repository.PostRepository
import com.example.graduation1.domain.models.AppPages
import com.example.graduation1.domain.models.PostData
import com.example.graduation1.postList
import com.example.graduation1.selectedPostPage
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.ui.theme.darkGray
import com.example.graduation1.ui.theme.primaryRed
import com.example.graduation1.viewmodel.PostViewModel
import com.example.graduation1.viewmodel.PostViewModelFactory


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController){

        val viewModel : PostViewModel = viewModel(
        factory = PostViewModelFactory(PostRepository(RetrofitInstance.api))
    )

    val postsList by viewModel.posts.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    val filteredPosts = remember (searchQuery, postsList) {
        if (searchQuery.isBlank()) postsList
        else postsList.filter { it.postText.contains(searchQuery, ignoreCase = true) }
    }

    val clipboard = LocalClipboardManager.current
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedPost by remember { mutableStateOf<PostData?>(null) }

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
                    .fillMaxWidth()
            ) {
                items(
                    filteredPosts,
                    key = {it.postId}) { post ->

                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .animateItem()
                                .clickable {
                                    selectedPostPage = post
                                    navController.navigate("${AppPages.Post.route}/${post.postId}") },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Image(
                                    painter = rememberAsyncImagePainter(post.groupImage),
                                    contentDescription = "GroupImage",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(shape = CircleShape)
                                )

                                Spacer(Modifier.width(10.dp))

                                Column {
                                    Text(
                                        text = post.groupName,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontSize = 16.sp
                                    )


                                    Text(
                                        text = post.publisherName,
                                        color = darkGray,
                                        fontSize = 14.sp,
                                        modifier = Modifier
                                            .clickable { navController.navigate("${AppPages.OtherUsersProfile.route}/${post.userId}") }
                                    )
                                }

                                Spacer(Modifier.weight(1f))

                                IconButton(onClick = {}) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.menudotshoriz),
                                        contentDescription = "menuDots",
                                        tint = MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            } // Row

                            Spacer(Modifier.height(10.dp))

                            Text(
                                text = post.postText,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp)
                                    .wrapContentHeight()
                            )

                            Spacer(Modifier.height(10.dp))

                            if (post.postImage != "") {
                                Image(
                                    painter = rememberAsyncImagePainter(post.postImage),
                                    contentDescription = "postImage",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                )

                                Spacer(Modifier.height(10.dp))
                            }

                            if (post.codeSnippet != "") {
                                Column(
                                   horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(28, 27, 27, 255))
                                ){

                                    Row(modifier = Modifier
                                        .fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically) {

                                        Spacer(Modifier.weight(1f))

                                        Button(onClick = {
                                            clipboard.setText(AnnotatedString(post.codeSnippet))
                                        },
                                            shape = RoundedCornerShape(0.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(79, 77, 77, 255)
                                            ),
                                            modifier = Modifier
                                                .width(100.dp)
                                                .height(40.dp)) {
                                            Text(text = stringResource(R.string.Copy),
                                                color = Color.White,
                                                fontSize = 13.sp)
                                        }
                                    } // Row

                                    Spacer(Modifier.height(10.dp))

                                    Text(text = post.codeSnippet,
                                        color = Color.White,
                                        fontSize = 15.sp)

                                } // Column

                                Spacer(Modifier.height(10.dp))
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = { viewModel.toggleLike(post.postId) }) {
                                        Icon(
                                            painter = if (!post.isLiked) painterResource(id = R.drawable.heart2)
                                            else painterResource(id = R.drawable.heart2red),
                                            contentDescription = "heart",
                                            tint = if (darkMode && !post.isLiked) Color.White else Color.Unspecified,
                                            modifier = Modifier.size(26.dp)
                                        )
                                    }

                                    Text(
                                        text = post.likesCount.toString(),
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,

                                    )
                                }

                                Spacer(Modifier.width(10.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = {
                                        selectedPost = post
                                        showBottomSheet = true
                                    }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.comment),
                                            contentDescription = "comment",
                                            tint = MaterialTheme.colorScheme.onBackground,
                                            modifier = Modifier.size(26.dp)
                                        )
                                    }

                                    Text(
                                        text = post.commentsCount.toString(),
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }

                                Spacer(Modifier.weight(1f))

                                IconButton(onClick = {viewModel.toggleSaved(post.postId)}) {
                                    Icon(
                                        painter = if (!post.isSaved) painterResource(id = R.drawable.saved)
                                        else painterResource(id = R.drawable.savedyellow),
                                        contentDescription = "saved",
                                        tint = if (darkMode && !post.isSaved) Color.White else Color.Unspecified,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }

                            } // Row
                        } // Column
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
                CommentsScreen(navController, selectedPost!!, viewModel)
            } // ModalBottomSheet
        } // if
    } // Box
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
fun HomeScreenPreview(){
    Graduation1Theme(dynamicColor = false) {
        val nav = rememberNavController()
        HomeScreen(nav)
    }
}