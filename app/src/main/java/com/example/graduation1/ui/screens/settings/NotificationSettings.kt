package com.example.graduation1.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
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

@Composable
fun NotificationSettingsScreen(navController: NavHostController){

    var generalNotifications by remember { mutableStateOf(false) }
    var sound by remember { mutableStateOf(false) }
    var vibrate by remember { mutableStateOf(false) }
    var appUpdates by remember { mutableStateOf(false) }
    var newServicesAvailable by remember { mutableStateOf(false) }
    var newTipsAvailable by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = {navController.popBackStack()}) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "back",
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(Modifier.weight(1f))

            Text(
                text = stringResource(R.string.Notifications),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.weight(1.5f))
        } // Row

        Spacer(Modifier.height(20.dp))

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = stringResource(R.string.GeneralNotifications),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
            )
            
            Spacer(Modifier.weight(1f))

            Switch(checked = generalNotifications,
                onCheckedChange = {generalNotifications = !generalNotifications})

        } // Row

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = stringResource(R.string.Sound),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
            )

            Spacer(Modifier.weight(1f))

            Switch(checked = sound,
                onCheckedChange = {sound = !sound})

        } // Row

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = stringResource(R.string.Vibrate),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
            )

            Spacer(Modifier.weight(1f))

            Switch(checked = vibrate,
                onCheckedChange = {vibrate = !vibrate})

        } // Row

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = stringResource(R.string.AppUpdates),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
            )

            Spacer(Modifier.weight(1f))

            Switch(checked = appUpdates,
                onCheckedChange = {appUpdates = !appUpdates})

        } // Row

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = stringResource(R.string.NewServicesAvailable),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
            )

            Spacer(Modifier.weight(1f))

            Switch(checked = newServicesAvailable,
                onCheckedChange = {newServicesAvailable = !newServicesAvailable})

        } // Row

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = stringResource(R.string.NewTipsAvailable),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
            )

            Spacer(Modifier.weight(1f))

            Switch(checked = newTipsAvailable,
                onCheckedChange = {newTipsAvailable = !newTipsAvailable})

        } // Row
    } // Column
}

@Composable
@Preview(showBackground = true, device = "spec:width=320dp,height=640dp")
fun NotificationSettingsScreenPreview(){
    Graduation1Theme(dynamicColor = false) {
        val nav = rememberNavController()
        NotificationSettingsScreen(nav)
    }
}