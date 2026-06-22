package com.example.graduation1.ui.screens.messaging

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.graduation1.R
import com.example.graduation1.data.remote.RetrofitInstance
import com.example.graduation1.data.repository.ChatRepository
import com.example.graduation1.domain.models.AppPages
import com.example.graduation1.domain.models.User
import com.example.graduation1.emptyProfileImage
import com.example.graduation1.language
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.ui.theme.darkGray
import com.example.graduation1.ui.theme.darkGreen
import com.example.graduation1.ui.theme.gray
import com.example.graduation1.ui.theme.primaryRed
import com.example.graduation1.user
import com.example.graduation1.viewmodel.ChatViewModel
import com.example.graduation1.viewmodel.ChatViewModelFactory
import com.example.graduation1.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SuspiciousIndentation")
@Composable
fun MessagingScreen(navController: NavHostController, chatId : String, chatViewModel: ChatViewModel, userViewModel: UserViewModel){
    val chatList by chatViewModel.chatsList.collectAsState()
    val chat = chatList.find { it.chatId == chatId } ?: return
    val userList by userViewModel.users.collectAsState()
    val user = userList.find { it.id == chat.userId } ?: return

    LaunchedEffect(Unit) {
        chatViewModel.getChatContent(user.id)
        chatViewModel.updateMessagesSeen(chatId)
    }

    val currentUser by chatViewModel.currentUser.collectAsState()

    val chatContent by chatViewModel.chatContent.collectAsState()
    val listState = rememberLazyListState()
    var firstLoad by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        val test = chatContent.filter { it.senderId == user.id }
        Log.d("TAG", "MessagingScreen is seen: $test")
    }

    val messageText by chatViewModel.messageText.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var cliackedMessage by remember { mutableStateOf("") }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var imageIsSelected by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = uri
            imageIsSelected = true
            //signUpViewModel.saveProfileImageUrlToFirebase(it)
        }
    }

    // when new message is received you scroll only if your screen is on the last message
    val isAtBottom by remember { derivedStateOf {
        val layoutInfo = listState.layoutInfo
        val totalItems = layoutInfo.totalItemsCount

        if (totalItems == 0) true

        else{
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible >= totalItems - 2
        }
    } }

    // auto scroll when new message is received
    LaunchedEffect(chatContent.size) {
        if (firstLoad && chatContent.isNotEmpty()) {
            listState.scrollToItem(chatContent.lastIndex)
            firstLoad = false
        }
        else if (isAtBottom) {
            listState.animateScrollToItem(chatContent.lastIndex)
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Card(elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(8.dp)
                    .clickable { navController.navigate("${AppPages.OtherUsersProfile.route}/${user.id}") },
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "back",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(Modifier.width(20.dp))

                Image(
                    if (user.image == "") rememberAsyncImagePainter(emptyProfileImage)
                    else rememberAsyncImagePainter(user.image),
                    contentDescription = "profile Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(shape = CircleShape)
                )

                Spacer(Modifier.width(10.dp))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        user.name,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = if (user.isOnline) stringResource(R.string.Online)
                        else stringResource(R.string.Offline),
                        color = gray,
                        fontSize = 14.sp
                    )
                }

                Spacer(Modifier.weight(3f))

            } // Row
        } // Card

        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .weight(1f),
            state = listState) {
            items(chatContent){ message ->
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .pointerInput(Unit){
                        detectTapGestures(
                            onLongPress = {
                                if (message.senderId == currentUser.id) {
                                    cliackedMessage = message.messageId
                                    showDeleteDialog = true }}
                        )
                    }) {

                    Card(modifier = Modifier
                        .widthIn(min = 10.dp, max = 280.dp)
                        .align(if (message.senderId == currentUser.id) Alignment.End else Alignment.Start),
                        shape = RoundedCornerShape(25.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (message.senderId == currentUser.id) primaryRed else darkGray
                        )) {

                        Column(modifier = Modifier.padding(10.dp)) {

                            if (message.image != null) {
                                Image(
                                    painter = rememberAsyncImagePainter(message.image),
                                    contentDescription = "messageImage",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .widthIn(100.dp, 300.dp)
                                        .heightIn(100.dp, 400.dp)
                                )

                                Spacer(Modifier.height(10.dp))
                            }

                            if (message.text.isNotEmpty()) {
                                Text(
                                    text = message.text,
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Row(horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically) {

                                Text(
                                    text = chatViewModel.getMessageTime(message.createdAt),
                                    color = Color.LightGray,
                                    fontSize = 11.sp,
                                    modifier = Modifier
                                        .padding(end = 6.dp)
                                )

                                if (message.senderId == currentUser.id)
                                Text(
                                    text = if (message.isSeen) stringResource(R.string.Seen)
                                    else stringResource(R.string.Sent),
                                    color = if (message.isSeen) darkGreen else Color.LightGray,
                                    fontSize = 11.sp,
                                    modifier = Modifier
                                )
                            } // Row
                        } // Column
                    } // Card
                } // column
            } // items
        } // LazyColumn

        Column {
            if (imageIsSelected){
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(100.dp),
                        contentAlignment = Alignment.Center){
                        Image(
                            painter = rememberAsyncImagePainter(selectedImageUri),
                            contentDescription = "image",
                            Modifier.size(100.dp)
                        )

                        IconButton(onClick = {
                            imageIsSelected = false
                            selectedImageUri = null
                        },
                            modifier = Modifier
                                .align(Alignment.TopEnd)) {
                            Icon(
                                painterResource(id = R.drawable.close),
                                contentDescription = "x",
                                tint = Color.Black,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color.White)
                            )
                        }
                    }
                    Spacer(Modifier.weight(1f))
                } // Row
            } // if
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                OutlinedTextField(
                    value = messageText,
                    onValueChange = { chatViewModel.updateMessageText(it) },
                    placeholder = {
                        Text(
                            stringResource(R.string.Message),
                            color = Color.Black,
                            maxLines = 1,
                            fontSize = 18.sp
                        )
                    },
                    shape = RoundedCornerShape(30.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    trailingIcon = {
                        Icon(
                            painterResource(id = R.drawable.clip),
                            contentDescription = "clip",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .size(18.dp)
                                .clickable { launcher.launch("image/*") }
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .weight(1f)
                )

                Spacer(Modifier.width(10.dp))

                IconButton(
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            if (messageText.isNotEmpty() || imageIsSelected)
                                if (selectedImageUri == null)
                                    chatViewModel.sendMessage(user.id, messageText, null)
                                else
                                    chatViewModel.sendMessage(user.id, messageText, selectedImageUri.toString())
                        }
                        chatViewModel.updateMessageText("")
                        selectedImageUri = null
                        imageIsSelected = false
                    },
                    colors = IconButtonDefaults.iconButtonColors(darkGray),
                    shape = CircleShape
                ) {
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
    } // Column

    if (showDeleteDialog){
        AlertDialog(
            onDismissRequest = {showDeleteDialog = false},
            title = { Text(stringResource(R.string.Alert)) },
            text = { Text(stringResource(R.string.sureDeleteComment)) },
            containerColor = MaterialTheme.colorScheme.background,
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    chatViewModel.removeMessage(cliackedMessage, chatId) }){
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
} // MessagingScreen

@Composable
@Preview(showBackground = true)
fun MessagingScreenPreview(){
    Graduation1Theme(dynamicColor = false) {
    }
}