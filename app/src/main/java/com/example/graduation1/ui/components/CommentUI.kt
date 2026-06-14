package com.example.graduation1.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.graduation1.R
import com.example.graduation1.darkMode
import com.example.graduation1.domain.models.AppPages
import com.example.graduation1.domain.models.Comment
import com.example.graduation1.ui.theme.darkGray
import com.example.graduation1.viewmodel.PostViewModel
import com.example.graduation1.viewmodel.UserViewModel
import kotlinx.coroutines.delay

@Composable
fun CommentUI(navController : NavHostController, comment: Comment, isNew : Boolean, postId : String, postViewModel : PostViewModel, userViewModel: UserViewModel){

    val currentUser = userViewModel.currentUser
    val userList by userViewModel.users.collectAsState()
    val user = userList.find { it.id == comment.userId } ?: return

    var highlight by remember(comment.commentId) { mutableStateOf(isNew) }
    LaunchedEffect(comment.commentId) {
        if (isNew) {
            delay(2000)
            highlight = false
            postViewModel.updateNewCommentId("")
        }
    }

    val backgroundColor by animateColorAsState(
        targetValue =
            if (highlight) MaterialTheme.colorScheme.surface
            else MaterialTheme.colorScheme.background
    )
    Column(
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .padding(14.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .clickable { navController.navigate("${AppPages.OtherUsersProfile.route}/${comment.userId}") },
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                rememberAsyncImagePainter(user.image),
                contentDescription = "image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(shape = CircleShape)
            )

            Text(
                modifier = Modifier
                    .padding(start = 8.dp),
                text = user.name,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.width(10.dp))

            Text(
                text = postViewModel.getTimeAgo(comment.createdAt),
                color = darkGray,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        } // Row

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier
                    .padding(start = 50.dp)
                    .weight(3f),
                text = comment.text,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)) {
                IconButton(onClick = {postViewModel.toggleLikeComment(comment.commentId, postId)}) {
                    Icon(
                        painter = if (!comment.likesCount.contains(currentUser.id)) painterResource(id = R.drawable.heart2)
                        else painterResource(id = R.drawable.heart2red),
                        contentDescription = "heart",
                        tint = if (darkMode && !comment.likesCount.contains(currentUser.id)) Color.White else Color.Unspecified,
                        modifier = Modifier.size(15.dp)
                    )
                }

                Text(
                    text = comment.likesCount.count().toString(),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .offset(y = (-15).dp)
                )
            } // Column
        } // Row
    } // Column
}