package com.example.graduation1.ui.screens.authentication

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.graduation1.R
import com.example.graduation1.domain.models.BottomNavItem
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.user
import com.example.graduation1.viewmodel.AuthViewModel

@Composable
fun CreateAccountScreen(navController: NavHostController, authViewModel: AuthViewModel){

    var username by remember { mutableStateOf("") }
    val email by authViewModel.email.collectAsState()
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val options = listOf(stringResource(R.string.Male), stringResource(R.string.Female))
    var selectedGender by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = uri
            //signUpViewModel.saveProfileImageUrlToFirebase(it)
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(rememberScrollState())
        .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "back",
                    modifier = Modifier
                        .size(30.dp)
                        .offset(x = (-10).dp)
                )
            }

            Spacer(Modifier.weight(1.5f))
        } // Row

        Spacer(Modifier.weight(1f))

        Box(
            modifier = Modifier
                .background(color = Color.Gray, shape = CircleShape)
                .size(110.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Image(
                rememberAsyncImagePainter(user.image),
                contentDescription = "image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(110.dp)
                    .clip(shape = CircleShape)
                    .clickable { launcher.launch("image/*") }
            )

            IconButton(onClick = { launcher.launch("image/*") },
                modifier = Modifier
                    .size(25.dp)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onBackground)) {
                Icon(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = "camera",
                    tint = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .size(25.dp)
                )
            }
        } // Box

        Spacer(Modifier.height(40.dp))

        Text(
            text = stringResource(R.string.Username),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.Start)
        )

        Spacer(Modifier.height(20.dp))

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp),
            value = username,
            onValueChange = { username = it },
            placeholder = {
                Text(
                    "",
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
                    painter = painterResource(id = R.drawable.personsettings),
                    contentDescription = "email",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(23.dp)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.Password),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp,
            modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.Start)
        )

        Spacer(Modifier.height(20.dp))

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp),
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("", color = MaterialTheme.colorScheme.onPrimary) },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimary),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.lock),
                    contentDescription = "lock",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(23.dp)
                )
            },
            trailingIcon = {
                IconButton(onClick = {isPasswordVisible = !isPasswordVisible}) {
                    Icon(painter = if (!isPasswordVisible) painterResource(id = R.drawable.visibility_off)
                    else painterResource(id = R.drawable.visibility),
                        contentDescription = "visibility",
                        tint = MaterialTheme.colorScheme.onBackground)
                } // IconButton
            }, // trailingIcon
            visualTransformation = if (isPasswordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.ConfirmPassword),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp,
            modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.Start)
        )

        Spacer(Modifier.height(20.dp))

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp),
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = { Text("", color = MaterialTheme.colorScheme.onPrimary) },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimary),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.lock),
                    contentDescription = "lock",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(23.dp)
                )
            },
            trailingIcon = {
                IconButton(onClick = {isConfirmPasswordVisible = !isConfirmPasswordVisible}) {
                    Icon(painter = if (!isConfirmPasswordVisible) painterResource(id = R.drawable.visibility_off)
                    else painterResource(id = R.drawable.visibility),
                        contentDescription = "visibility",
                        tint = MaterialTheme.colorScheme.onBackground)
                } // IconButton
            }, // trailingIcon
            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(Modifier.height(20.dp))

        Column(Modifier.fillMaxWidth()) {

            options.forEach { gender ->

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    RadioButton(
                        selected = selectedGender == gender,
                        onClick = {
                            selectedGender = gender
                        }

                    )

                    Text(gender)
                }
            }
        }

        Spacer(Modifier.height(40.dp))

        Button(onClick = {
            //authViewModel.createAccount(username, email, password)
            navController.navigate(BottomNavItem.Home.route)
        },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(start = 16.dp, end = 16.dp)) {
            Text(text = stringResource(R.string.Create_your_accunt),
                color = Color.White,
                fontSize = 18.sp)
        }

        Spacer(Modifier.weight(1f))
    } // Column
}

@Composable
@Preview(showBackground = true)
fun CreateAccountScreenPreview(){
    Graduation1Theme(dynamicColor = false) {
    }
}