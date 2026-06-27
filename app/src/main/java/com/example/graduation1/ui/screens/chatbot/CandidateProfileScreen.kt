package com.example.graduation1.ui.screens.chatbot

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.graduation1.viewmodel.ChatbotViewModel

@Composable
fun CandidateProfileScreen(
    navController: NavHostController,
    chatbotViewModel: ChatbotViewModel
) {
    val candidateProfile by chatbotViewModel.candidateProfile.collectAsState()
    val isSaving         by chatbotViewModel.isSavingCandidate.collectAsState()
    val error            by chatbotViewModel.error.collectAsState()

    // Pre-fill form with existing profile data if it exists
    var country            by remember { mutableStateOf(candidateProfile?.country            ?: "") }
    var governorate        by remember { mutableStateOf(candidateProfile?.governorate        ?: "") }
    var yearsText          by remember { mutableStateOf(candidateProfile?.yearsOfExperience?.toString() ?: "0") }
    var preferredRole      by remember { mutableStateOf(candidateProfile?.preferredRole      ?: "") }
    var bio                by remember { mutableStateOf(candidateProfile?.bio                ?: "") }
    var availabilityStatus by remember { mutableStateOf(candidateProfile?.availabilityStatus ?: "") }

    val availabilityOptions = listOf("Open to work", "Employed, open", "Not available")
    var showSuccess by remember { mutableStateOf(false) }

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Top bar
            Row(
                modifier          = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Text("←", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text       = if (candidateProfile == null) "Create Candidate Profile" else "Edit Candidate Profile",
                    fontSize   = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color      = MaterialTheme.colorScheme.onBackground
                )
            }

            Column(
                modifier              = Modifier.padding(horizontal = 20.dp),
                verticalArrangement   = Arrangement.spacedBy(14.dp)
            ) {
                if (candidateProfile != null) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape  = RoundedCornerShape(12.dp)
                    ) {
                        Column(Modifier.padding(14.dp)) {
                            Text("Current Profile", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(Modifier.height(4.dp))
                            Text("Role: ${candidateProfile!!.preferredRole ?: "—"}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("Status: ${candidateProfile!!.availabilityStatus ?: "—"}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("Experience: ${candidateProfile!!.yearsOfExperience} years", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }

                OutlinedTextField(
                    value         = country,
                    onValueChange = { country = it },
                    label         = { Text("Country") },
                    shape         = RoundedCornerShape(12.dp),
                    modifier      = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value         = governorate,
                    onValueChange = { governorate = it },
                    label         = { Text("Governorate / State") },
                    shape         = RoundedCornerShape(12.dp),
                    modifier      = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value         = preferredRole,
                    onValueChange = { preferredRole = it },
                    label         = { Text("Preferred Role (e.g. Android Developer)") },
                    shape         = RoundedCornerShape(12.dp),
                    modifier      = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value         = yearsText,
                    onValueChange = { if (it.all { c -> c.isDigit() }) yearsText = it },
                    label         = { Text("Years of Experience") },
                    shape         = RoundedCornerShape(12.dp),
                    modifier      = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value         = bio,
                    onValueChange = { bio = it },
                    label         = { Text("Bio / Summary") },
                    shape         = RoundedCornerShape(12.dp),
                    minLines      = 3,
                    modifier      = Modifier.fillMaxWidth()
                )

                // Availability chips
                Column {
                    Text("Availability", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        availabilityOptions.forEach { opt ->
                            val selected = availabilityStatus == opt
                            FilterChip(
                                selected = selected,
                                onClick  = { availabilityStatus = opt },
                                label    = { Text(opt, fontSize = 12.sp) }
                            )
                        }
                    }
                }

                // Error
                if (error != null) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        shape  = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(error ?: "", Modifier.weight(1f), color = MaterialTheme.colorScheme.onErrorContainer)
                            TextButton(onClick = { chatbotViewModel.clearError() }) { Text("Dismiss") }
                        }
                    }
                }

                // Success
                if (showSuccess) {
                    Card(
                        colors   = CardDefaults.cardColors(containerColor = Color(0xFF2E7D32).copy(alpha = 0.15f)),
                        shape    = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Profile saved successfully!",
                            Modifier.padding(14.dp),
                            color      = Color(0xFF2E7D32),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Button(
                    onClick = {
                        showSuccess = false
                        chatbotViewModel.saveCandidateProfile(
                            country            = country,
                            governorate        = governorate,
                            yearsOfExperience  = yearsText.toIntOrNull() ?: 0,
                            preferredRole      = preferredRole,
                            bio                = bio,
                            availabilityStatus = availabilityStatus,
                            onSuccess          = { showSuccess = true }
                        )
                    },
                    enabled  = !isSaving,
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
                            if (candidateProfile == null) "Create Profile" else "Save Changes",
                            fontSize   = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}