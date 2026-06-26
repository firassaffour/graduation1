package com.example.graduation1.ui.screens.chatbot

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.graduation1.R
import com.example.graduation1.viewmodel.ChatbotViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CodeReviewScreen(
    navController: NavHostController,
    chatbotViewModel: ChatbotViewModel
) {
    val codeReviewResult by chatbotViewModel.codeReviewResult.collectAsState()
    val isReviewing      by chatbotViewModel.isReviewing.collectAsState()
    val error            by chatbotViewModel.error.collectAsState()

    var code     by remember { mutableStateOf("") }
    var language by remember { mutableStateOf("") }

    val clipboard = LocalClipboardManager.current
    val codeEditorColor = Color(28, 27, 27, 255)

    // Clear previous result when the screen opens
    LaunchedEffect(Unit) { chatbotViewModel.clearCodeReview() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ── Top bar ──────────────────────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                text = "Code Reviewer",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.weight(1.5f))
        }

        Spacer(Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {

            // ── Language hint field ──────────────────────────────────────────
            OutlinedTextField(
                value = language,
                onValueChange = { language = it },
                placeholder = { Text("Language (optional): kotlin, python, js …") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            // ── Code editor ──────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp, max = 400.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(codeEditorColor)
            ) {
                TextField(
                    value = code,
                    onValueChange = { code = it },
                    placeholder = {
                        Text(
                            "Paste or type your code here…",
                            color = Color.Gray,
                            fontFamily = FontFamily.Monospace
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor   = codeEditorColor,
                        unfocusedContainerColor = codeEditorColor,
                        focusedIndicatorColor   = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor        = Color.White,
                        unfocusedTextColor      = Color.White
                    ),
                    textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 200.dp, max = 400.dp)
                )

                // Copy button (top-right of editor)
                TextButton(
                    onClick = { clipboard.setText(AnnotatedString(code)) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                ) {
                    Text("Copy", color = Color.LightGray, fontSize = 12.sp)
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Review button ─────────────────────────────────────────────────
            Button(
                onClick = {
                    chatbotViewModel.submitCodeForReview(
                        code     = code,
                        language = language.ifBlank { null }
                    )
                },
                enabled = code.isNotBlank() && !isReviewing,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                if (isReviewing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Reviewing…", fontSize = 16.sp)
                } else {
                    Text("Review My Code", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            // ── Error snackbar ────────────────────────────────────────────────
            if (error != null) {
                Spacer(Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = error ?: "",
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = { chatbotViewModel.clearError() }) {
                            Text("Dismiss")
                        }
                    }
                }
            }

            // ── AI feedback card ──────────────────────────────────────────────
            AnimatedVisibility(
                visible = codeReviewResult != null,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
            ) {
                val feedback = codeReviewResult?.content ?: ""
                Spacer(Modifier.height(16.dp))

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "🤖  AI Feedback",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.weight(1f))
                            // Copy feedback button
                            IconButton(onClick = {
                                clipboard.setText(AnnotatedString(feedback))
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.terms),
                                    contentDescription = "copy feedback",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(Modifier.height(10.dp))
                        Divider()
                        Spacer(Modifier.height(10.dp))

                        Text(
                            text = feedback,
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(Modifier.height(14.dp))

                        // "Review another" resets state
                        OutlinedButton(
                            onClick = {
                                chatbotViewModel.clearCodeReview()
                                code = ""
                                language = ""
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Review Another Snippet")
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        } // scrollable column
    } // outer column
}