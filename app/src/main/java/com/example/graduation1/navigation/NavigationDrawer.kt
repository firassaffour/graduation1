package com.example.graduation1.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.graduation1.R
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.ui.theme.primaryRed

@Composable
fun NavigationDrawer(navController: NavHostController, closeDrawer : () -> Unit){


    Column(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth(fraction = 0.8f)
        .navigationBarsPadding()
        .background(color = MaterialTheme.colorScheme.background)){
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(color = MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Spacer(Modifier.height(20.dp))

            IconButton(onClick = closeDrawer,
                modifier = Modifier.align(Alignment.Start)) {
                Icon(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "close",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(35.dp)
                )
            }

            Spacer(Modifier.height(50.dp))

            Image(
                painter = rememberAsyncImagePainter("https://nextluxury.com/wp-content/uploads/funny-profile-pictures-4.jpg"),
                contentDescription = "profileImage",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(shape = CircleShape)
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Trump",
                color = primaryRed,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(20.dp))

            Spacer(Modifier.weight(1f))

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    painter = painterResource(id = R.drawable.settings),
                    contentDescription = "settings",
                    tint = primaryRed,
                    modifier = Modifier.size(30.dp)
                )

                Spacer(Modifier.width(20.dp))

                Text(
                    text = "Settings",
                    color = primaryRed,
                    fontSize = 20.sp,
                    maxLines = 1
                )

            } // Row
        } // Column
    } // Column
}

@Composable
@Preview(showBackground = true)
fun NavigationDrawerPreview(){
    Graduation1Theme(darkTheme = true, dynamicColor = false) {
        val nav = rememberNavController()
        NavigationDrawer(nav){}
    }
}