package com.example.graduation1.ui.screens.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.graduation1.R
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.ui.theme.primaryRed

@Composable
fun OnBoarding2Screen(){

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Spacer(Modifier.weight(1f))

        Text(
            text = stringResource(R.string.PowerfulFeatures),
            color = primaryRed,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.EverythingYouNeedInOnePlace),
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(50.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {

            Spacer(Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .width(120.dp)
                    .height(160.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.weight(1f))

                Image(
                    painterResource(R.drawable.chatbotlogo),
                    contentDescription = "image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(shape = CircleShape)
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = stringResource(R.string.ChatBot),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.weight(1f))
            } // Column

            Spacer(Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .width(120.dp)
                    .height(160.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.weight(1f))

                Image(
                    painterResource(R.drawable.phonechat),
                    contentDescription = "image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(shape = CircleShape)
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = stringResource(R.string.Rooms),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.weight(1f))
            } // Column

            Spacer(Modifier.weight(1f))
        } // Row

        Spacer(Modifier.height(30.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {

            Spacer(Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .width(120.dp)
                    .height(160.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.weight(1f))

                Image(
                    painterResource(R.drawable.people),
                    contentDescription = "image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(shape = CircleShape)
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = stringResource(R.string.Groups),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.weight(1f))
            } // Column

            Spacer(Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .width(120.dp)
                    .height(160.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.weight(1f))

                Image(
                    painterResource(R.drawable.chatcolored),
                    contentDescription = "image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(shape = CircleShape)
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = stringResource(R.string.Chatting),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.weight(1f))
            } // Column

            Spacer(Modifier.weight(1f))
        } // Row

        Spacer(Modifier.weight(1f))
    } // Column
}

@Composable
@Preview(showBackground = true)
fun OnBoarding2ScreenPreview(){
    Graduation1Theme(dynamicColor = false) {
        OnBoarding2Screen()
    }
}