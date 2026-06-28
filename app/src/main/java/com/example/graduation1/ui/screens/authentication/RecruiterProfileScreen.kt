package com.example.graduation1.ui.screens.authentication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.graduation1.R
import com.example.graduation1.domain.models.AppPages
import com.example.graduation1.viewmodel.ChatbotViewModel

@Composable
fun RecruiterProfileScreen(
    navController: NavHostController,
    chatbotViewModel: ChatbotViewModel
) {
    val recruiterProfile by chatbotViewModel.recruiterProfile.collectAsState()
    val isSaving         by chatbotViewModel.isSavingRecruiter.collectAsState()
    val error            by chatbotViewModel.error.collectAsState()

    var companyName    by remember { mutableStateOf(recruiterProfile?.companyName    ?: "") }
    var companyWebsite by remember { mutableStateOf(recruiterProfile?.companyWebsite ?: "") }
    var location       by remember { mutableStateOf(recruiterProfile?.location       ?: "") }
    var about          by remember { mutableStateOf(recruiterProfile?.about          ?: "") }

    var showSuccess by remember { mutableStateOf(false) }

    val isEditMode = recruiterProfile != null

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            Row(
                modifier          = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter            = painterResource(id = R.drawable.back),
                        contentDescription = "back",
                        modifier           = Modifier.size(28.dp),
                        tint               = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text       = if (isEditMode) "Edit Recruiter Profile" else "Become a Recruiter",
                    fontSize   = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color      = MaterialTheme.colorScheme.onBackground
                )
            }

            Column(
                modifier            = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                if (isEditMode) {
                    Card(
                        colors   = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape    = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                "Current Profile",
                                fontWeight = FontWeight.Bold,
                                color      = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                "Company: ${recruiterProfile!!.companyName}",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (!recruiterProfile!!.location.isNullOrBlank())
                                Text(
                                    "Location: ${recruiterProfile!!.location}",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                        }
                    }
                } else {
                    Card(
                        colors   = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        shape    = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                "Why create a recruiter profile?",
                                fontWeight = FontWeight.Bold,
                                color      = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "A recruiter profile lets you post jobs, view ranked candidates, " +
                                        "and access the full analytics dashboard.",
                                color      = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontSize   = 14.sp
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value         = companyName,
                    onValueChange = { companyName = it; showSuccess = false },
                    label         = { Text("Company Name *") },
                    placeholder   = { Text("e.g. Vortex Labs") },
                    shape         = RoundedCornerShape(12.dp),
                    singleLine    = true,
                    isError       = companyName.isBlank() && !isSaving,
                    modifier      = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value         = companyWebsite,
                    onValueChange = { companyWebsite = it; showSuccess = false },
                    label         = { Text("Company Website (optional)") },
                    placeholder   = { Text("https://example.com") },
                    shape         = RoundedCornerShape(12.dp),
                    singleLine    = true,
                    modifier      = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value         = location,
                    onValueChange = { location = it; showSuccess = false },
                    label         = { Text("Location (optional)") },
                    placeholder   = { Text("e.g. Cairo, Egypt") },
                    shape         = RoundedCornerShape(12.dp),
                    singleLine    = true,
                    modifier      = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value         = about,
                    onValueChange = { about = it; showSuccess = false },
                    label         = { Text("About the company (optional)") },
                    placeholder   = { Text("Brief description of your company…") },
                    shape         = RoundedCornerShape(12.dp),
                    minLines      = 3,
                    modifier      = Modifier.fillMaxWidth()
                )

                if (error != null) {
                    Card(
                        colors   = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        shape    = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                error ?: "",
                                Modifier.weight(1f),
                                color    = MaterialTheme.colorScheme.onErrorContainer,
                                fontSize = 14.sp
                            )
                            TextButton(onClick = { chatbotViewModel.clearError() }) {
                                Text("Dismiss")
                            }
                        }
                    }
                }

                if (showSuccess) {
                    Card(
                        colors   = CardDefaults.cardColors(containerColor = Color(0xFF2E7D32).copy(alpha = 0.15f)),
                        shape    = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (isEditMode) "Profile updated successfully!"
                            else            "Recruiter profile created! You can now view candidate rankings.",
                            Modifier.padding(14.dp),
                            color      = Color(0xFF2E7D32),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Button(
                    onClick = {
                        if (companyName.isBlank()) return@Button
                        showSuccess = false
                        chatbotViewModel.saveRecruiterProfile(
                            companyName    = companyName,
                            companyWebsite = companyWebsite,
                            location       = location,
                            about          = about,
                            onSuccess      = { showSuccess = true }
                        )
                    },
                    enabled  = companyName.isNotBlank() && !isSaving,
                    shape    = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            modifier    = Modifier.size(20.dp),
                            color       = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Saving…")
                    } else {
                        Text(
                            if (isEditMode) "Save Changes" else "Create Recruiter Profile",
                            fontSize   = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (showSuccess && !isEditMode) {
                    OutlinedButton(
                        onClick  = { navController.navigate(AppPages.CandidateRanking.route) },
                        shape    = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth().height(52.dp)
                    ) {
                        Text(
                            "Go to Candidate Ranking →",
                            fontSize   = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}