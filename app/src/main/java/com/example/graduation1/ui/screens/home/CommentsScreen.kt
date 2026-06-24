package com.example.graduation1.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.graduation1.R
import com.example.graduation1.language
import com.example.graduation1.ui.components.CommentUI
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.ui.theme.darkGray
import com.example.graduation1.viewmodel.PostViewModel
import com.example.graduation1.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CommentsScreen(navController : NavHostController, postId : String, postViewModel: PostViewModel, userViewModel: UserViewModel){

    val postsList by postViewModel.posts.collectAsState()
    val commentText by postViewModel.commentText.collectAsState()
    val newCommentId by postViewModel.newCommentId.collectAsState()
    val comments by postViewModel.comments.collectAsState()
    val isLoading by postViewModel.isCommentsLoading.collectAsState()

    val post = postsList.find { it.postId == postId } ?: return

    val listState = rememberLazyListState()
    LaunchedEffect(comments.size) {
        if (comments.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    LaunchedEffect(Unit) {
        postViewModel.getCommentsByPost(postId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = stringResource(R.string.Comments),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(15.dp))

        if (isLoading){
            Spacer(Modifier.height(300.dp))
            CircularProgressIndicator()
        }

        else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                state = listState
            ) {
                items(comments, key = { it.commentId }) { comment ->
                    CommentUI(
                        navController,
                        comment,
                        comment.commentId == newCommentId,
                        post.postId,
                        postViewModel,
                        userViewModel
                    )
                } // items
            } // LazyColumn
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically) {

            OutlinedTextField(
                value = commentText,
                onValueChange = { postViewModel.updateCommentText(it) },
                placeholder = { Text(stringResource(R.string.Comment), maxLines = 1, fontSize = 18.sp) },
                shape = RoundedCornerShape(30.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .weight(1f)
            )

            Spacer(Modifier.width(10.dp))

            IconButton(onClick = {
                if (commentText.isNotEmpty())
                  postViewModel.createComment(post.postId) },
                colors = IconButtonDefaults.iconButtonColors(darkGray),
                shape = CircleShape) {
                Icon(
                    painter = painterResource(id = R.drawable.sendarrow),
                    contentDescription = "send",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(18.dp)
                        .rotate(if (language == "ar") 180f else 0f)
                )
            }
        } // Row
    } // Column
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
fun CommentsScreenPreview(){
    Graduation1Theme(dynamicColor = false) {

    }
}