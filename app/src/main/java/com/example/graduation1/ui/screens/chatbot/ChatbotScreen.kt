package com.example.graduation1.ui.screens.chatbot

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.graduation1.data.repository.ChatRepository
import com.example.graduation1.data.repository.ChatbotRepository
import com.example.graduation1.domain.models.AppPages
import com.example.graduation1.language
import com.example.graduation1.messageList
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.ui.theme.darkGray
import com.example.graduation1.ui.theme.gray
import com.example.graduation1.ui.theme.primaryRed
import com.example.graduation1.viewmodel.ChatViewModel
import com.example.graduation1.viewmodel.ChatViewModelFactory
import com.example.graduation1.viewmodel.ChatbotViewModel
import com.example.graduation1.viewmodel.ChatbotViewModelFactory

@Composable
fun ChatbotScreen(navController: NavHostController, chatbotViewModel: ChatbotViewModel){

    val chatContent by chatbotViewModel.messages.collectAsState()
    val listState = rememberLazyListState()
    var firstLoad by remember { mutableStateOf(true) }

    val messageText by chatbotViewModel.messageText.collectAsState()

    val currentUser by chatbotViewModel.currentUser.collectAsState()

    val isLoading by chatbotViewModel.isLoading.collectAsState()

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var imageIsSelected by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = uri
            //signUpViewModel.saveProfileImageUrlToFirebase(it)
        }
    }

    // when new message is received you scroll only if your screen is on the last message
    val shouldAutoScroll by remember { derivedStateOf {
        val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
        lastVisible >= listState.layoutInfo.totalItemsCount - 2
    } }

    // auto scroll when new message is received
    LaunchedEffect(chatContent.size, shouldAutoScroll) {
        if (firstLoad && chatContent.isNotEmpty()) {
            listState.scrollToItem(chatContent.size - 1)
            firstLoad = false
        }
        if (shouldAutoScroll) {
            val lastIndex = chatContent.size
            if (lastIndex > 0) listState.animateScrollToItem(lastIndex - 1)
        }
    }


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "back",
                    modifier = Modifier.size(30.dp)
                )
            }
            
            Spacer(Modifier.weight(1.5f))

            Image(
                painter = painterResource(id = R.drawable.chatbotlogo),
                contentDescription = "logo",
                modifier = Modifier
                    .size(50.dp)
                    .offset(y = 20.dp)
            )

            Spacer(Modifier.weight(1f))

            IconButton(onClick = { navController.navigate(AppPages.ChatbotHistory.route) }) {
                Icon(
                    painter = painterResource(id = R.drawable.history),
                    contentDescription = "history",
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(Modifier.width(10.dp))

            Text(
                text = "Code",
                color = gray,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .clickable { navController.navigate(AppPages.CodeReview.route) }
            )

            Spacer(Modifier.width(10.dp))

            Text(
                text = stringResource(R.string.Jobs),
                color = gray,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .clickable { navController.navigate(AppPages.JobsList.route) }
            )

            Spacer(Modifier.width(10.dp))
        } // Row

        Spacer(Modifier.height(20.dp))

        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
            state = listState) {
            items(chatContent){ message ->
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)) {

                    Card(modifier = Modifier
                        .widthIn(min = 10.dp, max = 300.dp)
                        .align(if (message.senderId == currentUser.id) Alignment.End else Alignment.Start),
                        shape = RoundedCornerShape(25.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (message.senderId == currentUser.id) primaryRed else gray
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
                        } // Column
                    } // Card
                } // column
            } // items

            if (isLoading){
                item{
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)) {

                        Card(modifier = Modifier
                            .widthIn(min = 10.dp, max = 300.dp)
                            .align(Alignment.Start),
                            shape = RoundedCornerShape(25.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = gray
                            )) {

                            Column(modifier = Modifier.padding(10.dp)) {

                                    Text(
                                        text = "Thinking...",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                            } // Column
                        } // Card
                    } // column
                }
            }
        } // LazyColumn

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { chatbotViewModel.updateMessageText(it) },
                    placeholder = {
                        Text(
                            stringResource(R.string.AskWhatsOnMind),
                            color = Color.Black,
                            maxLines = 1,
                            fontSize = 18.sp
                        )
                    },
                    shape = RoundedCornerShape(30.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = gray,
                        unfocusedContainerColor = gray
                    ),
                    modifier = Modifier
                        .weight(1f)
                )

                Spacer(Modifier.width(5.dp))
                    IconButton(
                        onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            if (messageText.isNotEmpty() || imageIsSelected)
                                if (selectedImageUri == null)
                                    chatbotViewModel.sendMessage()
                                else
                                    chatbotViewModel.sendMessage()
                        }
                                chatbotViewModel.updateMessageText("")
                                selectedImageUri = null
                                imageIsSelected = false
                },
                        colors = IconButtonDefaults.iconButtonColors(Color(47, 127, 236, 255)),
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
}

@Composable
@Preview(showBackground = true)
fun ChatbotScreenPreview(){
    Graduation1Theme(dynamicColor = false) {
        val nav = rememberNavController()
    }
}