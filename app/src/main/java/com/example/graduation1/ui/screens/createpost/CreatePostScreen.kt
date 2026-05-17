package com.example.graduation1.ui.screens.createpost

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
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
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.user

@Composable
fun CreatePostScreen(navController: NavHostController){

    var postText by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {

            IconButton(onClick = {navController.popBackStack()}) {
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
                    rememberAsyncImagePainter(user.image),
                    contentDescription = "profile Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(35.dp)
                        .clip(shape = CircleShape)
                )
            } // Card

            Spacer(Modifier.width(10.dp))

            Text(
                text = stringResource(R.string.Anyone),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp
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

            Button(onClick = {},
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

        TextField(
            value = postText,
            onValueChange = { postText = it },
            placeholder = { Text(stringResource(R.string.Share_thoughts), maxLines = 1, fontSize = 20.sp) },
            shape = RoundedCornerShape(30.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
                focusedIndicatorColor = MaterialTheme.colorScheme.background
            ),
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )



        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {

            Spacer(Modifier.weight(1f))

            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(id = R.drawable.addimage),
                    contentDescription = "add image",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(35.dp)
                )
            }

            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(id = R.drawable.plus),
                    contentDescription = "plus",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(35.dp)
                )
            }
        }
    } // Column
}

@Composable
@Preview(showBackground = true)
fun CreatePostScreenPreview(){
    Graduation1Theme(dynamicColor = false) {
        val nav = rememberNavController()
        CreatePostScreen(nav)
    }
}