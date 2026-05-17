package com.example.graduation1.ui.screens.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.graduation1.R
import com.example.graduation1.domain.models.AppPages
import com.example.graduation1.language
import com.example.graduation1.ui.theme.Graduation1Theme

@Composable
fun StartScreen(navController: NavHostController){

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.background)
        .padding(8.dp)
        .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Spacer(Modifier.height(40.dp))

        Image(
            painter = painterResource(id = R.drawable.logoimage),
            contentDescription = "logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .fillMaxHeight(0.3f)
        )

        Spacer(Modifier.height(60.dp))

        Text(
            text = stringResource(R.string.Glad_to_have_you_with_us) ,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp,
            modifier = Modifier
                .fillMaxWidth()
        )

        Text(
            text =
                if (language == "en") stringResource(R.string.with_us)
            else "",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
        )

        Spacer(Modifier.height(60.dp))

        Button(onClick = {navController.navigate(AppPages.SignUp.route)},
            shape = RoundedCornerShape(17.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(8.dp)) {
            Text(text = stringResource(R.string.Continue),
                color = MaterialTheme.colorScheme.background,
                fontSize = 26.sp)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun StartScreenPreview(){
    Graduation1Theme(dynamicColor = false) {
        val nav = rememberNavController()
        StartScreen(nav)
    }
}