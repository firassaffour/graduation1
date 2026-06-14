package com.example.graduation1.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.graduation1.R
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.viewmodel.UserViewModel

@Composable
fun LocationScreen(navController: NavHostController, userViewModel: UserViewModel){

    val currentUser = userViewModel.currentUser
    var selected by remember { mutableStateOf(currentUser.location) }
    val options = listOf(
        stringResource(R.string.Egypt),
        stringResource(R.string.Syria),
        stringResource(R.string.Libya),
        stringResource(R.string.Sudan),
        stringResource(R.string.Lebanon),
        stringResource(R.string.SaudiArabia),
        stringResource(R.string.Palestine),
    )

    Column(modifier = Modifier
        .fillMaxSize()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "back",
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(Modifier.weight(1f))

            Text(
                text = stringResource(R.string.Location),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.weight(1.5f))
        } // Row

        Spacer(Modifier.height(20.dp))

        options.forEach { location ->

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                RadioButton(
                    selected = selected == location,
                    onClick = {
                        selected = location

                    }
                )

                Text(location)
            }
        }
    } // Column
}
