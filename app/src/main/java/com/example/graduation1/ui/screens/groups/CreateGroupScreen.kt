package com.example.graduation1.ui.screens.groups

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.graduation1.R
import com.example.graduation1.domain.models.BottomNavItem
import com.example.graduation1.ui.theme.gray
import com.example.graduation1.viewmodel.GroupsViewModel

@Composable
fun CreateGroupScreen(navController: NavHostController, groupsViewModel: GroupsViewModel){

    var showCancelPostDialog by remember { mutableStateOf(false) }
    val groupName by groupsViewModel.groupName.collectAsState()
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = uri
            //signUpViewModel.saveProfileImageUrlToFirebase(it)
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = {
                if (groupName.isNotEmpty() || selectedImageUri != null) showCancelPostDialog = !showCancelPostDialog
                else navController.popBackStack()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "close",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(Modifier.weight(1f))

            Button(onClick = {
               /* when {
                    postText.isEmpty() || postText.isBlank() -> Toast.makeText(context, emptyPostTextMessage, Toast.LENGTH_SHORT).show()
                    codeSnippetAdded && postCodeSnippet.isEmpty() || codeSnippetAdded && postCodeSnippet.isBlank() -> Toast.makeText(context, emptyCodeMessage, Toast.LENGTH_SHORT).show()
                    selectedGroup == null -> Toast.makeText(context, emptyGroupMessage, Toast.LENGTH_SHORT).show()

                    selectedImageUri == null ->{
                        postViewModel.createPost(selectedGroup!!.id, selectedGroup!!.name, selectedGroup!!.image.toString(), "")
                        groupsViewModel.updateSelectedGroup(null)
                        Toast.makeText(context, postedSuccessfullyMessage, Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                        navController.navigate(BottomNavItem.Home.route)
                    }
                    else -> {
                        postViewModel.createPost(selectedGroup!!.id, selectedGroup!!.name, selectedGroup!!.image.toString(), selectedImageUri.toString())
                        groupsViewModel.updateSelectedGroup(null)
                        Toast.makeText(context, postedSuccessfullyMessage, Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                        navController.navigate(BottomNavItem.Home.route)
                    }
                } */
            },
                shape = RoundedCornerShape(17.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .wrapContentWidth()
                    .height(40.dp)) {
                Text(text = stringResource(R.string.Create),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold)
            }
        } // Row

        Spacer(Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(gray)
                .heightIn(min = 30.dp, max = 300.dp)
                .clickable { launcher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {

            if (selectedImageUri == null){
                Text(text = stringResource(R.string.AddImage),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold)
            }
            else {
                Image(
                    painter = rememberAsyncImagePainter(selectedImageUri),
                    contentDescription = "image",
                    Modifier
                        .fillMaxWidth()
                        .heightIn(min = 30.dp, max = 300.dp)
                )

                IconButton(
                    onClick = {
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
            }
        } // Box

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = groupName,
            onValueChange = { groupsViewModel.updateGroupName(it) },
            placeholder = { Text(stringResource(R.string.GroupName), fontSize = 20.sp) },
            shape = RoundedCornerShape(30.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
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
                    groupsViewModel.updateGroupName("")
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