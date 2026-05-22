package com.example.graduation1.ui.screens.createpost

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.graduation1.R
import com.example.graduation1.domain.models.AppPages
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.viewmodel.GroupsViewModel
import com.example.graduation1.viewmodel.PostViewModel
import com.example.graduation1.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreatePostScreen(navController: NavHostController, userViewModel: UserViewModel, postViewModel: PostViewModel, groupsViewModel: GroupsViewModel){

    // App Context
    val context = LocalContext.current

    // Post View Model
    val postText by postViewModel.postText.collectAsState()
    val postCodeSnippet by postViewModel.postCodeSnippet.collectAsState()

    // Groups View Model
    val selectedGroup by groupsViewModel.selectedGroup.collectAsState()

    val currentUser = userViewModel.currentUser

    // Screen Variables
    var codeSnippetAdded by rememberSaveable { mutableStateOf(false) }

    var selectedImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var imageIsSelected by rememberSaveable { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = uri
            imageIsSelected = true
            //signUpViewModel.saveProfileImageUrlToFirebase(it)
        }
    }

    var showCancelPostDialog by remember { mutableStateOf(false) }

    val emptyPostTextMessage = stringResource(R.string.Write_Something)
    val emptyCodeMessage = stringResource(R.string.Write_Code_Please)
    val emptyGroupMessage = stringResource(R.string.Please_Select_Group)
    val postedSuccessfullyMessage = stringResource(R.string.Posted_Successfully)




    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {

            IconButton(onClick = {
                if (postText.isNotEmpty() || selectedGroup != null) showCancelPostDialog = !showCancelPostDialog
                else navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "close",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(30.dp)
                )
            }

            Card(modifier = Modifier.size(35.dp),
                shape = RoundedCornerShape(25.dp)) {

                Image(
                    if (selectedGroup != null) rememberAsyncImagePainter(selectedGroup!!.image)
                    else painterResource(R.drawable.create),
                    contentDescription = "group Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(35.dp)
                        .clip(shape = CircleShape)
                        .clickable { navController.navigate("${AppPages.GroupsList.route}/${currentUser.id}") }
                )
            } // Card

            Spacer(Modifier.width(10.dp))

            Text(
                text = if (selectedGroup != null) selectedGroup!!.name
                else stringResource(R.string.Select_Group),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
                modifier = Modifier
                    .clickable { navController.navigate("${AppPages.GroupsList.route}/${currentUser.id}") }
            )

            Spacer(Modifier.weight(1f))

            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(id = R.drawable.hour),
                    contentDescription = "hour",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(25.dp)
                )
            }

            Button(onClick = {
                    when {
                        postText.isEmpty() || postText.isBlank() -> Toast.makeText(context, emptyPostTextMessage, Toast.LENGTH_SHORT).show()
                        codeSnippetAdded && postCodeSnippet.isEmpty() || postCodeSnippet.isBlank() -> Toast.makeText(context, emptyCodeMessage, Toast.LENGTH_SHORT).show()
                        selectedGroup == null -> Toast.makeText(context, emptyGroupMessage, Toast.LENGTH_SHORT).show()

                        selectedImageUri == null ->{
                            postViewModel.createPost(selectedGroup!!.id, selectedGroup!!.name, selectedGroup!!.image.toString(), "")
                            groupsViewModel.updateSelectedGroup(null)
                            Toast.makeText(context, postedSuccessfullyMessage, Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                        else -> {
                            postViewModel.createPost(selectedGroup!!.id, selectedGroup!!.name, selectedGroup!!.image.toString(), selectedImageUri.toString())
                            groupsViewModel.updateSelectedGroup(null)
                            Toast.makeText(context, postedSuccessfullyMessage, Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    }
            },
                shape = RoundedCornerShape(17.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .wrapContentWidth()
                    .height(40.dp)) {
                Text(text = stringResource(R.string.Post),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold)
            }
        } // Row

        Spacer(Modifier.width(10.dp))

        Column(modifier = Modifier
            .weight(1f)
            .verticalScroll(rememberScrollState())) {

            TextField(
                value = postText,
                onValueChange = { postViewModel.updatePostText(it) },
                placeholder = { Text(stringResource(R.string.Share_thoughts), fontSize = 20.sp) },
                shape = RoundedCornerShape(30.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
                    focusedIndicatorColor = MaterialTheme.colorScheme.background
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )

            if (codeSnippetAdded) {
                TextField(
                    value = postCodeSnippet,
                    onValueChange = { postViewModel.updatePostCodeSnippet(it) },
                    placeholder = { Text(stringResource(R.string.Write_Code), fontSize = 20.sp) },
                    shape = RoundedCornerShape(30.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(28, 27, 27, 255),
                        unfocusedContainerColor = Color(28, 27, 27, 255),
                        unfocusedIndicatorColor = Color(28, 27, 27, 255),
                        focusedIndicatorColor = Color(28, 27, 27, 255),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
            }

            if (imageIsSelected) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 30.dp, max = 300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImageUri),
                        contentDescription = "image",
                        Modifier
                            .fillMaxWidth()
                            .heightIn(min = 30.dp, max = 300.dp)
                    )

                    IconButton(
                        onClick = {
                            imageIsSelected = false
                            selectedImageUri = null
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                    ) {
                        Icon(
                            painterResource(id = R.drawable.close),
                            contentDescription = "x",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(25.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                    }
                } // Box
            } // if
        } // Column

        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {

            Spacer(Modifier.weight(1f))

            IconButton(onClick = {launcher.launch("image/*")}) {
                Icon(
                    painter = painterResource(id = R.drawable.addimage),
                    contentDescription = "add image",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(35.dp)
                )
            }

            IconButton(onClick = {codeSnippetAdded = !codeSnippetAdded}) {
                Icon(
                    painter = if (!codeSnippetAdded) painterResource(id = R.drawable.plus)
                    else painterResource(R.drawable.close),
                    contentDescription = "plus",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(35.dp)
                )
            }
        } // Row
    } // Column
    if (showCancelPostDialog){
        AlertDialog(
            onDismissRequest = {showCancelPostDialog = false},
            title = { Text(stringResource(R.string.Alert)) },
            text = { Text(stringResource(R.string.Are_your_sure_close_post)) },
            containerColor = MaterialTheme.colorScheme.background,
            confirmButton = {
                TextButton(onClick = {
                    navController.popBackStack()
                    postViewModel.updatePostText("")
                    postViewModel.updatePostCodeSnippet("")
                    groupsViewModel.updateSelectedGroup(null)
                }){
                    Text(stringResource(R.string.Yes))
                }
            },
            dismissButton = {
                TextButton(onClick = {showCancelPostDialog = false}){
                    Text(stringResource(R.string.No))
                }
            } // dismissButton
        ) // AlertDialog
    } // if
}

@Composable
@Preview(showBackground = true)
fun CreatePostScreenPreview(){
    Graduation1Theme(dynamicColor = false) {
    }
}