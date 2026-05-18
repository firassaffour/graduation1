package com.example.graduation1.ui.screens.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.graduation1.R
import com.example.graduation1.domain.models.AppPages
import com.example.graduation1.domain.models.BottomNavItem
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.ui.theme.gray

@Composable
fun OnBoarding3Screen(navController: NavHostController){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Spacer(Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.done),
            contentDescription = "done",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.15f)
        )

        Spacer(Modifier.height(40.dp))

        Text(
            text = stringResource(R.string.YouAreAllSet),
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 33.sp
        )

        Spacer(Modifier.height(30.dp))

        Text(
            text = stringResource(R.string.StartYourProgrammingJournay),
            color = gray,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            lineHeight = 40.sp
        )

        Spacer(Modifier.height(80.dp))

        Button(onClick = {
            navController.navigate(AppPages.StartScreen.route)
        },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .height(50.dp)) {
            Text(text = stringResource(R.string.GetStarted),
                color = Color.White,
                fontSize = 25.sp)
        }

        Spacer(Modifier.weight(1f))
    } // Column
}

@Composable
@Preview(showBackground = true)
fun OnBoarding3ScreenPreview(){
    Graduation1Theme(dynamicColor = false) {
        val nav = rememberNavController()
        OnBoarding3Screen(nav)
    }
}