package com.example.graduation1.ui.screens.chatbot

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.graduation1.R
import com.example.graduation1.domain.models.AppPages
import com.example.graduation1.ui.theme.Graduation1Theme

@Composable
fun ChatbotStartScreen(navController: NavHostController){

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)
        .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Spacer(Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.chatbotlogo),
            contentDescription = "logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .fillMaxHeight(0.3f)
        )

        Spacer(Modifier.weight(1f))


        Button(onClick = {navController.navigate(AppPages.Chatbot.route)},
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(8.dp)) {
            Text(text = stringResource(R.string.GetStarted),
                color = MaterialTheme.colorScheme.background,
                fontSize = 24.sp)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ChatbotStartPreview(){
    Graduation1Theme(dynamicColor = false) {
        val nav = rememberNavController()
        ChatbotStartScreen(nav)
    }
}