package com.example.graduation1.ui.components

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.graduation1.R
import com.example.graduation1.darkMode
import com.example.graduation1.domain.models.AppPages
import com.example.graduation1.domain.models.PostData
import com.example.graduation1.ui.screens.home.PostAnalysisDialog
import com.example.graduation1.ui.theme.darkGray
import com.example.graduation1.viewmodel.ChatbotViewModel
import com.example.graduation1.viewmodel.GroupsViewModel
import com.example.graduation1.viewmodel.PostViewModel
import com.example.graduation1.viewmodel.UserViewModel
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostUI(navController: NavHostController,
           post: PostData,
           isNew : Boolean,
           postViewModel: PostViewModel,
           userViewModel: UserViewModel,
           groupsViewModel: GroupsViewModel,
           chatbotViewModel: ChatbotViewModel,
           onPostClicked : () -> Unit,
           onCommentClicked : () -> Unit,
           onGroupClicked : () -> Unit,
           onPostDeleted : () -> Unit){

    val userList by userViewModel.users.collectAsState()
    val user = userList.find { it.id == post.userId } ?: return
    val currentUser by userViewModel.currentUser.collectAsState()

    val groupList by groupsViewModel.groups.collectAsState()
    val group = groupList.find { it.id == post.groupId } ?: return

    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current

    var showDeleteDialog by remember { mutableStateOf(false) }
    val postDeletedMessage = stringResource(R.string.PostDeletedSuccessfully)

    val postAnalysis  by chatbotViewModel.postAnalysis.collectAsState()
    val isAnalyzing   by chatbotViewModel.isAnalyzing.collectAsState()
    var showAnalysisDialog by remember { mutableStateOf(false) }

    var highlight by remember(post.postId) { mutableStateOf(isNew) }
    LaunchedEffect(post.postId) {
        if (isNew) {
            delay(2000)
            highlight = false
            postViewModel.updateNewPostId("")
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
            .padding(8.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        if (post.userId == currentUser.id) showDeleteDialog = true
                    },
                    onTap = { onPostClicked() }
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            if (post.userId == currentUser.id) showDeleteDialog = true
                        },
                        onTap = { onPostClicked() }
                    )
                },
        ) {

            Image(
                painter = rememberAsyncImagePainter(group.image),
                contentDescription = "GroupImage",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(shape = CircleShape)
                    .clickable { onGroupClicked() }
            )

            Spacer(Modifier.width(10.dp))

            Column {
                Text(
                    text = group.name,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .clickable { onGroupClicked() }
                )


                Text(
                    text = user.name,
                    color = darkGray,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .clickable {if (user.id == currentUser.id) navController.navigate(AppPages.MyProfileDetails.route)
                            else navController.navigate("${AppPages.OtherUsersProfile.route}/${post.userId}") }
                )
            }

            Spacer(Modifier.weight(1f))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Text(
                    text = postViewModel.getTimeAgo(post.createdAt),
                    color = darkGray,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset(y = (-10).dp)
                )
            } // Column
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
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            if (post.userId == currentUser.id) showDeleteDialog = true
                        },
                        onTap = { onPostClicked() }
                    )
                },
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
                IconButton(onClick = {
                    postViewModel.toggleLike(post.postId)
                    postViewModel.refreshData()}) {
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
                IconButton(onClick = {onCommentClicked()}) {
                    Icon(
                        painter = painterResource(id = R.drawable.comment),
                        contentDescription = "comment",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            IconButton(onClick = {postViewModel.toggleSaved(post)}) {
                Icon(
                    painter = if (!post.isSaved) painterResource(id = R.drawable.saved)
                    else painterResource(id = R.drawable.savedyellow),
                    contentDescription = "saved",
                    tint = if (darkMode && !post.isSaved) Color.White else Color.Unspecified,
                    modifier = Modifier.size(30.dp)
                )
            }

            if (post.codeSnippet.isNotBlank()) {
                IconButton(onClick = {
                    chatbotViewModel.analyzePost(post.postId.toInt())
                    showAnalysisDialog = true
                }) {
                    if (isAnalyzing) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        Icon(
                            painter            = painterResource(R.drawable.chatbot),  // use any suitable AI/analysis icon
                            contentDescription = "Analyze code",
                            tint               = MaterialTheme.colorScheme.onBackground,
                            modifier           = Modifier.size(24.dp)
                        )
                    }
                }
            }

        } // Row
    } // Column

    if (showDeleteDialog){
        AlertDialog(
            onDismissRequest = {showDeleteDialog = false},
            title = { Text(stringResource(R.string.Alert)) },
            text = { Text(stringResource(R.string.sureDeletePost)) },
            containerColor = MaterialTheme.colorScheme.background,
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    postViewModel.removePost(post.postId)
                    Toast.makeText(context, postDeletedMessage, Toast.LENGTH_SHORT).show()
                    onPostDeleted()}){
                    Text(stringResource(R.string.Yes))
                }
            },
            dismissButton = {
                TextButton(onClick = {showDeleteDialog = false}){
                    Text(stringResource(R.string.No))
                }
            } // dismissButton
        ) // AlertDialog
    } // if

    if (showAnalysisDialog) {
        PostAnalysisDialog(
            analysis  = postAnalysis,
            isLoading = isAnalyzing,
            onDismiss = {
                showAnalysisDialog = false
                chatbotViewModel.clearPostAnalysis()
            }
        )
    }

}