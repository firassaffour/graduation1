package com.example.graduation1.ui.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import com.example.graduation1.domain.models.AppPages
import com.example.graduation1.selectedPostPage
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.ui.theme.darkGray
import com.example.graduation1.viewmodel.PostViewModel
import com.example.graduation1.viewmodel.PostViewModelFactory

@Composable
fun SavedScreen(navController: NavHostController){


    val viewModel : PostViewModel = viewModel(
        factory = PostViewModelFactory(PostRepository(RetrofitInstance.api))
    )

    val savedPostsList by viewModel.savedPosts.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getSavedPosts()
    }

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

            Text(
                text = stringResource(R.string.Saved),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.weight(1.5f))
        } // Row

        Spacer(Modifier.height(20.dp))

        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)) {
            items(savedPostsList){ post ->

                Column(
                    modifier = Modifier
                        .padding(8.dp)
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
                            contentDescription = "groupImage",
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
                        maxLines = 2,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                            .wrapContentHeight()
                    )
                } // Column
            } // items
        } // LazyColumn
    } // Column
}

@Composable
@Preview(showBackground = true)
fun SavedScreenPreview(){
    Graduation1Theme(dynamicColor = false) {
        val nav = rememberNavController()
        SavedScreen(nav)
    }
}