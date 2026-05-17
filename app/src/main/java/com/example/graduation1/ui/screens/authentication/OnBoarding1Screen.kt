package com.example.graduation1.ui.screens.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.graduation1.R
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.ui.theme.primaryRed

@Composable
fun OnBoarding1Screen(){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryRed),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Spacer(Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.braintech),
            contentDescription = "logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.15f)
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.Let_your_learning_begin),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 33.sp
        )

        Spacer(Modifier.height(20.dp))

        Image(
            painter = painterResource(id = R.drawable.binaryicon),
            contentDescription = "logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.3f)
                .offset(x = (-40).dp, y = 40.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.binaryicon),
            contentDescription = "logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.5f)
                .offset(x = 40.dp, y = (-100).dp)
        )

        Spacer(Modifier.weight(1f))
    } // Column
}

@Composable
@Preview(showBackground = true)
fun OnBoarding1ScreenPreview(){
    Graduation1Theme(dynamicColor = false) {
        OnBoarding1Screen()
    }
}