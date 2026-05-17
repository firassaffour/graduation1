package com.example.graduation1.ui.screens.courses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.ui.theme.primaryRed

@Composable
fun CoursesScreen(){

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.background)) {

        Row(modifier = Modifier
            .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = "Chat",
                color = primaryRed,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )
        } // Row

        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.secondary)
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {

        } // Column
    } // Column
}

@Composable
@Preview(showBackground = true)
fun CoursesScreenPreview(){
    Graduation1Theme(darkTheme = true, dynamicColor = false) {
        CoursesScreen()
    }
}