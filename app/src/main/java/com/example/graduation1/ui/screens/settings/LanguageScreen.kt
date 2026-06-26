package com.example.graduation1.ui.screens.settings

import android.app.Activity
import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.graduation1.R
import com.example.graduation1.data.remote.RetrofitInstance
import com.example.graduation1.data.repository.PostRepository
import com.example.graduation1.data.repository.UserRepository
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.viewmodel.PostViewModel
import com.example.graduation1.viewmodel.PostViewModelFactory
import com.example.graduation1.viewmodel.UserViewModel
import com.example.graduation1.viewmodel.UserViewModelFactory

@Composable
fun LanguageScreen(navController: NavHostController, userViewModel: UserViewModel) {
    val context = LocalContext.current
    val activity = context as Activity
    val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    val options = listOf("English", "العربية")
    var selected by remember { mutableStateOf(
        if (prefs.getString("lang", "en") == "ar") "العربية"
    else "English") }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

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
                text = stringResource(R.string.Language),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.weight(1.5f))
        } // Row

        Spacer(Modifier.height(20.dp))

        options.forEach { lang ->

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                RadioButton(
                    selected = selected == lang,
                    onClick = {
                        selected = lang

                        if (lang == "العربية") {
                            userViewModel.setAppLanguage(context, "ar")
                            activity.recreate()
                        } else {
                            userViewModel.setAppLanguage(context, "en")
                            activity.recreate()
                        }
                    }
                )

                Text(lang)
            }
        }
    } // Column
} // LanguageScreen

@Composable
@Preview(showBackground = true)
fun LanguageScreenPreview(){

}