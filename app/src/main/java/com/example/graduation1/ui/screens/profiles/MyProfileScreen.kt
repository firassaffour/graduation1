package com.example.graduation1.ui.screens.profiles

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.graduation1.R
import com.example.graduation1.domain.models.AppPages
import com.example.graduation1.emptyProfileImage
import com.example.graduation1.language
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.user
import com.example.graduation1.viewmodel.AuthViewModel
import com.example.graduation1.viewmodel.UserViewModel

@Composable
fun MyProfileScreen(navController: NavHostController, userViewModel: UserViewModel, authViewModel: AuthViewModel){

    var showClearCacheDialog by remember { mutableStateOf(false) }
    var showClearHistoryDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val currentUser by userViewModel.currentUser.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {navController.popBackStack()}) {
                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "back",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(Modifier.weight(1f))

                Text(
                    text = stringResource(R.string.Myprofile),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.weight(1f))

                IconButton(onClick = {navController.navigate(AppPages.Settings.route)}) {
                    Icon(
                        painter = painterResource(id = R.drawable.settings),
                        contentDescription = "settings",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(30.dp)
                    )
                }
            } // Row

            Spacer(Modifier.height(10.dp))

            Row {
                Box(modifier = Modifier
                    .background(color = Color.Gray, shape = CircleShape)
                    .size(110.dp)) {

                    Image(
                        if (currentUser.image == "") rememberAsyncImagePainter(emptyProfileImage)
                        else rememberAsyncImagePainter(currentUser.image),
                        contentDescription = "image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(110.dp)
                            .clip(shape = CircleShape)
                            .clickable { navController.navigate(AppPages.MyProfileDetails.route) }
                    )
                }

                Spacer(Modifier.weight(1f))

                Column(
                    modifier = Modifier
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        text = currentUser.name,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                    )

                    Spacer(Modifier.height(20.dp))

                    Button(onClick = {navController.navigate(AppPages.EditProfile.route)},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier) {
                        Text(text = stringResource(R.string.EditProfile),
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 14.sp)
                    }
                } // Column

                Spacer(Modifier.weight(1f))
            } // Row

            Spacer(Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { navController.navigate(AppPages.Favourite.route) }) {
                Icon(
                    painter = painterResource(id = R.drawable.heart),
                    contentDescription = "heart",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(25.dp)
                )

                Spacer(Modifier.width(20.dp))

                Text(
                    text = stringResource(R.string.Favourite),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.weight(1f))

                Icon(
                    painter = painterResource(id = R.drawable.rightarrow),
                    contentDescription = "right arrow",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(30.dp)
                        .rotate(if (language == "ar") 180f else 0f)
                )
            } // Row

            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { navController.navigate(AppPages.Saved.route) }) {
                Icon(
                    painter = painterResource(id = R.drawable.saved),
                    contentDescription = "saved",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(27.dp)
                )

                Spacer(Modifier.width(20.dp))

                Text(
                    text = stringResource(R.string.Saved),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.weight(1f))

                Icon(
                    painter = painterResource(id = R.drawable.rightarrow),
                    contentDescription = "right arrow",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(30.dp)
                        .rotate(if (language == "ar") 180f else 0f)
                )
            } // Row

            Spacer(Modifier.height(20.dp))

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = Color.Gray
            )

            Spacer(Modifier.height(20.dp))

            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { navController.navigate(AppPages.Language.route) }) {
                Icon(
                    painter = painterResource(id = R.drawable.language),
                    contentDescription = "language",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(25.dp)
                )

                Spacer(Modifier.width(20.dp))

                Text(
                    text = stringResource(R.string.Language),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.weight(1f))

                Icon(
                    painter = painterResource(id = R.drawable.rightarrow),
                    contentDescription = "right arrow",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(30.dp)
                        .rotate(if (language == "ar") 180f else 0f)
                )
            } // Row

            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { navController.navigate(AppPages.Location.route) }) {
                Icon(
                    painter = painterResource(id = R.drawable.location2),
                    contentDescription = "location",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(25.dp)
                )

                Spacer(Modifier.width(20.dp))

                Text(
                    text = stringResource(R.string.Location),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.weight(1f))

                Icon(
                    painter = painterResource(id = R.drawable.rightarrow),
                    contentDescription = "right arrow",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(30.dp)
                        .rotate(if (language == "ar") 180f else 0f)
                )
            } // Row

            Spacer(Modifier.height(20.dp))

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = Color.Gray
            )

            Spacer(Modifier.height(20.dp))

            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        authViewModel.logout(context)
                        navController.navigate(AppPages.StartScreen.route){popUpTo(0)}
                    }) {
                Icon(
                    painter = painterResource(id = R.drawable.logout),
                    contentDescription = "logout",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(25.dp)
                )

                Spacer(Modifier.width(20.dp))

                Text(
                    text = stringResource(R.string.Logout),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.weight(1f))

                Icon(
                    painter = painterResource(id = R.drawable.rightarrow),
                    contentDescription = "right arrow",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(30.dp)
                        .rotate(if (language == "ar") 180f else 0f)
                )
            } // Row
        } // Column
    } // Column

    if (showClearCacheDialog){
        AlertDialog(
            onDismissRequest = {showClearCacheDialog = false},
            title = { Text(stringResource(R.string.Alert)) },
            text = { Text(stringResource(R.string.sureClearCache)) },
            containerColor = MaterialTheme.colorScheme.background,
            confirmButton = {
                TextButton(onClick = {showClearCacheDialog = false}){
                    Text(stringResource(R.string.Yes))
                }
            },
            dismissButton = {
                TextButton(onClick = {showClearCacheDialog = false}){
                    Text(stringResource(R.string.No))
                }
            } // dismissButton
        ) // AlertDialog
    } // if

    if (showClearHistoryDialog){
        AlertDialog(
            onDismissRequest = {showClearHistoryDialog = false},
            title = { Text(stringResource(R.string.Alert)) },
            text = { Text(stringResource(R.string.sureClearHistory)) },
            containerColor = MaterialTheme.colorScheme.background,
            confirmButton = {
                TextButton(onClick = {showClearHistoryDialog = false}){
                    Text(stringResource(R.string.Yes))
                }
            },
            dismissButton = {
                TextButton(onClick = {showClearHistoryDialog = false}){
                    Text(stringResource(R.string.No))
                }
            } // dismissButton
        ) // AlertDialog
    } // if
}

@Composable
@Preview(showBackground = true)
fun MyProfileScreenPreview(){
    Graduation1Theme(dynamicColor = false) {
        val nav = rememberNavController()
    }
}