package com.example.graduation1.ui.screens.authentication

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.graduation1.R
import com.example.graduation1.domain.models.AppPages
import com.example.graduation1.domain.models.BottomNavItem
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.ui.theme.primaryRed
import com.example.graduation1.ui.theme.secondaryBlue
import com.example.graduation1.viewmodel.AuthViewModel

@Composable
fun SignUpScreen(navController: NavHostController, authViewModel: AuthViewModel){

    var phoneButtonClicked by remember { mutableStateOf(false) }
    var googleButtonClicked by remember { mutableStateOf(false) }
    var facebookButtonClicked by remember { mutableStateOf(false) }
    var emailButtonClicked by remember { mutableStateOf(true) }
    var continueButtonClicked by remember { mutableStateOf(false) }

    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .background(color = MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Spacer(Modifier.weight(1f))

            Text(
                text = stringResource(R.string.SignUp),
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.Create_your_accunt),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp
            )

            Spacer(Modifier.height(30.dp))

            if (emailButtonClicked) {
            Text(
                text = stringResource(R.string.Email_Address),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(Modifier.height(20.dp))

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                value = email,
                onValueChange = { email = it },
                placeholder = {
                    Text(
                        "name@example.com",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 18.sp
                    )
                },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary
                ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.email),
                        contentDescription = "email",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(23.dp)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
        }

            if (phoneButtonClicked){
                Text(
                    text = stringResource(R.string.PhoneNumber),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(Modifier.height(20.dp))

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    placeholder = {
                        Text(
                            "EX +201158****",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 18.sp
                        )
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedTextColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.phone),
                            contentDescription = "phone",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(23.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
            }

            Spacer(Modifier.height(20.dp))

            Button(onClick = {
                navController.navigate(AppPages.CreateAccount.route)
                             },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)) {
                Text(text = stringResource(R.string.Continue),
                    color = Color.White,
                    fontSize = 18.sp)
            }

            Spacer(Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Divider(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = stringResource(R.string.Or),
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Divider(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )
            } // Divider

            Spacer(Modifier.height(10.dp))

            Surface(
                onClick = {
                            emailButtonClicked = !emailButtonClicked
                            phoneButtonClicked = !phoneButtonClicked

                          },
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onBackground)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                        .animateContentSize(
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = LinearOutSlowInEasing
                            )
                        ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (emailButtonClicked) {
                        Icon(
                            painter = painterResource(id = R.drawable.phone),
                            contentDescription = "phone",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(23.dp)
                        )

                        Spacer(Modifier.weight(1f))

                        Text(
                            stringResource(R.string.Continue_with_phone),
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    if (phoneButtonClicked) {
                        Icon(
                            painter = painterResource(id = R.drawable.email),
                            contentDescription = "email",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(23.dp)
                        )

                        Spacer(Modifier.weight(1f))

                        Text(
                            stringResource(R.string.Continue_with_email),
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                        Spacer(Modifier.weight(1f))
                } // Row
            } // Surface

            Spacer(Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.AlreadyHaveAccount),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(start = 16.dp)
            )

            Text(
                text = stringResource(R.string.Login),
                color = secondaryBlue,
                fontSize = 16.sp,
                modifier = Modifier
                    .clickable { navController.navigate(AppPages.Login.route) }
            )

            Spacer(Modifier.weight(1f))
        } // Column
}

@Composable
@Preview(showBackground = true)
fun SignUpScreenPreview(){
    Graduation1Theme( dynamicColor = false) {
        val nav = rememberNavController()
    }
}