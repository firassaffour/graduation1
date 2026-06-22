package com.example.graduation1.ui.screens.settings

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.background
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.graduation1.R
import com.example.graduation1.darkMode
import com.example.graduation1.domain.models.AppPages
import com.example.graduation1.language
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.viewmodel.AuthViewModel

@Composable
fun SettingsScreen(navController: NavHostController, authViewModel: AuthViewModel){

    val context = LocalContext.current
    val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    var showDeleteAccountDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()) {

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically) {

            IconButton(onClick = {navController.popBackStack()}) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "back",
                    modifier = Modifier.size(30.dp)
                )
            }

                Spacer(Modifier.weight(1f))

                Text(text = stringResource(R.string.Settings),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold)

            Spacer(Modifier.weight(1.5f))
        } // Row

        Spacer(Modifier.height(20.dp))

        Row(modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .height(50.dp)
            .clickable { navController.navigate(AppPages.ReportTabs.route) },
            verticalAlignment = Alignment.CenterVertically) {

            Spacer(Modifier.width(5.dp))

            Icon(
                painter = painterResource(id = R.drawable.chart),
                contentDescription = "report",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(25.dp)
            )

            Spacer(Modifier.width(15.dp))

            Text(
                text = stringResource(R.string.Report),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp
            )

            Spacer(Modifier.weight(1f))

            Icon(
                painter = painterResource(id = R.drawable.rightarrow),
                contentDescription = "arrow",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(25.dp)
                    .rotate(if (language == "ar") 180f else 0f)
            )
        } // Row
        Divider(
            modifier = Modifier
                .height(1.dp),
            color = Color.Black
        )

        Row(modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .height(50.dp)
            .clickable { navController.navigate(AppPages.Password.route) },
            verticalAlignment = Alignment.CenterVertically) {

            Spacer(Modifier.width(5.dp))

            Icon(
                painter = painterResource(id = R.drawable.lock),
                contentDescription = "lock",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(25.dp)
            )

            Spacer(Modifier.width(15.dp))

            Text(
                text = stringResource(R.string.Password),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp
            )

            Spacer(Modifier.weight(1f))

            Icon(
                painter = painterResource(id = R.drawable.rightarrow),
                contentDescription = "arrow",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(25.dp)
                    .rotate(if (language == "ar") 180f else 0f)
            )
        } // Row

        Divider(
            modifier = Modifier
                .height(1.dp),
            color = Color.Black
        )

        Row(modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .height(50.dp)
            .clickable { navController.navigate(AppPages.NotificationSettings.route) },
            verticalAlignment = Alignment.CenterVertically) {

            Spacer(Modifier.width(5.dp))

            Icon(
                painter = painterResource(id = R.drawable.bell),
                contentDescription = "bell",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(25.dp)
            )

            Spacer(Modifier.width(15.dp))

            Text(
                text = stringResource(R.string.Notifications),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp
            )

            Spacer(Modifier.weight(1f))

            Icon(
                painter = painterResource(id = R.drawable.rightarrow),
                contentDescription = "arrow",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(25.dp)
                    .rotate(if (language == "ar") 180f else 0f)
            )
        } // Row

        Divider(
            modifier = Modifier
                .height(1.dp),
            color = Color.Black
        )

        Row(modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .height(50.dp),
            verticalAlignment = Alignment.CenterVertically) {

            Spacer(Modifier.width(5.dp))

            Icon(
                painter = painterResource(id = R.drawable.moon),
                contentDescription = "moon",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(25.dp)
            )

            Spacer(Modifier.width(15.dp))

            Text(
                text = stringResource(R.string.DarkMode),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp
            )

            Spacer(Modifier.weight(1f))

            Switch(checked = darkMode,
                onCheckedChange = {
                    darkMode = !darkMode
                prefs.edit() {putBoolean("dark", darkMode)}
                })
        } // Row

        Spacer(Modifier.height(100.dp))

        Row(modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .height(50.dp)
            .clickable { navController.navigate(AppPages.AboutApplication.route) },
            verticalAlignment = Alignment.CenterVertically) {

            Spacer(Modifier.width(5.dp))

            Icon(
                painter = painterResource(id = R.drawable.about),
                contentDescription = "about",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(25.dp)
            )

            Spacer(Modifier.width(15.dp))

            Text(
                text = stringResource(R.string.AboutApplication),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp
            )

            Spacer(Modifier.weight(1f))

            Icon(
                painter = painterResource(id = R.drawable.rightarrow),
                contentDescription = "arrow",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(25.dp)
                    .rotate(if (language == "ar") 180f else 0f)
            )
        } // Row

        Divider(
            modifier = Modifier
                .height(1.dp),
            color = Color.Black
        )

        Row(modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .height(50.dp)
            .clickable { navController.navigate(AppPages.FAQ.route) },
            verticalAlignment = Alignment.CenterVertically) {

            Spacer(Modifier.width(5.dp))

            Icon(
                painter = painterResource(id = R.drawable.cloud),
                contentDescription = "cloud",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(25.dp)
            )

            Spacer(Modifier.width(15.dp))

            Text(
                text = "Help/FAQ",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp
            )

            Spacer(Modifier.weight(1f))

            Icon(
                painter = painterResource(id = R.drawable.rightarrow),
                contentDescription = "arrow",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(25.dp)
                    .rotate(if (language == "ar") 180f else 0f)
            )
        } // Row

        Divider(
            modifier = Modifier
                .height(1.dp),
            color = Color.Black
        )

        Row(modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .height(50.dp)
            .clickable { showDeleteAccountDialog = true },
            verticalAlignment = Alignment.CenterVertically) {

            Spacer(Modifier.width(5.dp))

            Icon(
                painter = painterResource(id = R.drawable.trash),
                contentDescription = "trash",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(25.dp)
            )

            Spacer(Modifier.width(15.dp))

            Text(
                text = stringResource(R.string.DeleteMyAccount),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp
            )

            Spacer(Modifier.weight(1f))

            Icon(
                painter = painterResource(id = R.drawable.rightarrow),
                contentDescription = "arrow",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(25.dp)
                    .rotate(if (language == "ar") 180f else 0f)
            )
        } // Row
    } // Column

    if (showDeleteAccountDialog){
        AlertDialog(
            onDismissRequest = {showDeleteAccountDialog = false},
            title = { Text(stringResource(R.string.Alert)) },
            text = { Text(stringResource(R.string.sureDeleteAccount)) },
            containerColor = MaterialTheme.colorScheme.background,
            confirmButton = {
                TextButton(onClick = {showDeleteAccountDialog = false}){
                    Text(stringResource(R.string.Yes))
                }
            },
            dismissButton = {
                TextButton(onClick = {showDeleteAccountDialog = false}){
                    Text(stringResource(R.string.No))
                }
            } // dismissButton
        ) // AlertDialog
    } // if
}

@Composable
@Preview(showBackground = true)
fun SettingsScreenPreview(){
    Graduation1Theme(dynamicColor = false) {
        val nav = rememberNavController()
    }
}