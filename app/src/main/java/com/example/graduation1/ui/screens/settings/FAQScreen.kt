package com.example.graduation1.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.graduation1.R
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.ui.theme.primaryRed

@Composable
fun FAQScreen(navController: NavHostController){

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = {navController.popBackStack()}) {
                Icon(
                    painter = painterResource(id = R.drawable.rightarrow),
                    contentDescription = "back",
                    modifier = Modifier
                        .size(30.dp)
                        .rotate(180f)
                )
            }
        } // Row

        Text(text = "FAQ and Support",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(start = 40.dp))

        Spacer(Modifier.height(20.dp))

        Text(text = "Didn’t find the answer you were looking for?\ncontact our support",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 40.dp))

        Spacer(Modifier.height(50.dp))

        Row(modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable {  },
            verticalAlignment = Alignment.CenterVertically) {

            Spacer(Modifier.width(11.dp))

            Icon(
                painter = painterResource(id = R.drawable.world),
                contentDescription = "world",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(20.dp)
            )

            Spacer(Modifier.width(15.dp))

            Text(
                text = "Go to Our Website",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        } // Row

        Row(modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable {  },
            verticalAlignment = Alignment.CenterVertically) {

            Spacer(Modifier.width(5.dp))

            Icon(
                painter = painterResource(id = R.drawable.email),
                contentDescription = "email",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(30.dp)
            )

            Spacer(Modifier.width(15.dp))

            Text(
                text = "Email Us",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        } // Row

        Spacer(Modifier.height(20.dp))

        Divider(
            modifier = Modifier
                .height(1.dp),
            color = Color.Black
        )

        Spacer(Modifier.height(20.dp))

        Row(modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable {  },
            verticalAlignment = Alignment.CenterVertically) {

            Spacer(Modifier.width(11.dp))

            Icon(
                painter = painterResource(id = R.drawable.terms),
                contentDescription = "terms",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(20.dp)
            )

            Spacer(Modifier.width(15.dp))


            Text(
                text = "Terms and Service",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        } // Row

        Spacer(Modifier.height(20.dp))

        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .background(primaryRed)) {

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable {  },
                    verticalAlignment = Alignment.CenterVertically) {

                    Spacer(Modifier.width(11.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = "search",
                        tint = Color.White,
                        modifier = Modifier
                            .size(20.dp)
                    )

                    Spacer(Modifier.width(15.dp))


                    Text(
                        text = "Find Question...",
                        color = MaterialTheme.colorScheme.background,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                } // Row
            } // Column
        } // Card

        Spacer(Modifier.height(20.dp))

        Row(modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
            .clickable {  },
            verticalAlignment = Alignment.CenterVertically) {

            Spacer(Modifier.width(15.dp))

            Text(
                text = "how do i change my password?",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.weight(1f))

            Icon(
                painter = painterResource(id = R.drawable.rightarrow),
                contentDescription = "arrow",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(20.dp)
                    .rotate(90f)
            )
        } // Row
    } // Column
}

@Composable
@Preview(showBackground = true)
fun FAQScreenPreview(){
    Graduation1Theme(dynamicColor = false) {
        val nav = rememberNavController()
        FAQScreen(nav)
    }
}