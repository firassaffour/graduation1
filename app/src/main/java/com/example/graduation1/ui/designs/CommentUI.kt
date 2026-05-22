package com.example.graduation1.ui.designs

import androidx.compose.foundation.Image
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

@Composable
fun CommentUI(navController : NavHostController, comment: Comment, postId : String, viewModel : PostViewModel){

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate("${AppPages.OtherUsersProfile.route}/${comment.id}") },
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                rememberAsyncImagePainter(comment.image),
                contentDescription = "image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(shape = CircleShape)
            )

            Text(
                modifier = Modifier
                    .padding(start = 8.dp),
                text = comment.name,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.width(10.dp))

            Text(
                text = comment.date,
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
                IconButton(onClick = {viewModel.toggleLikeComment(comment.id, postId)}) {
                    Icon(
                        painter = if (!comment.isLiked) painterResource(id = R.drawable.heart2)
                        else painterResource(id = R.drawable.heart2red) ,
                        contentDescription = "heart",
                        tint = if (darkMode && !comment.isLiked) Color.White else Color.Unspecified,
                        modifier = Modifier.size(15.dp)
                    )
                }

                Text(
                    text = comment.likesCount.toString(),
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