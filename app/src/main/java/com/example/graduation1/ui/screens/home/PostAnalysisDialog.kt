package com.example.graduation1.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.graduation1.domain.models.requets_response.PostAnalysisResponse

@Composable
fun PostAnalysisDialog(
    analysis: PostAnalysisResponse?,
    isLoading: Boolean,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape  = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(Modifier.padding(20.dp)) {

                Text(
                    "Code Analysis",
                    fontSize   = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color      = MaterialTheme.colorScheme.onSurface
                )

                Spacer(Modifier.height(12.dp))

                if (isLoading || analysis == null) {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(Modifier.height(8.dp))
                            Text("Analysing code…", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                } else {
                    // Score ring
                    val scoreColor = when {
                        analysis.score >= 80 -> Color(0xFF2E7D32)
                        analysis.score >= 50 -> Color(0xFFF9A825)
                        else                 -> Color(0xFFC62828)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(50))
                                .background(scoreColor.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "${analysis.score}",
                                fontSize   = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color      = scoreColor
                            )
                        }
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(
                                if (analysis.isCorrect) "✅ Correct" else "❌ Has issues",
                                fontWeight = FontWeight.Bold,
                                color      = if (analysis.isCorrect) Color(0xFF2E7D32) else Color(0xFFC62828)
                            )
                            Text(
                                "Confidence: ${analysis.confidence}%",
                                color    = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 13.sp
                            )
                            if (!analysis.difficulty.isNullOrBlank()) {
                                Text(
                                    "Difficulty: ${analysis.difficulty}",
                                    color    = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }

                    if (!analysis.reason.isNullOrBlank()) {
                        Spacer(Modifier.height(12.dp))
                        Divider()
                        Spacer(Modifier.height(8.dp))
                        Text("Reason", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(Modifier.height(4.dp))
                        Text(analysis.reason, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    if (!analysis.correction.isNullOrBlank()) {
                        Spacer(Modifier.height(12.dp))
                        Text("Suggested correction", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(Modifier.height(4.dp))
                        Text(analysis.correction, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    if (analysis.tags.isNotEmpty()) {
                        Spacer(Modifier.height(12.dp))
                        Text("Tags", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            analysis.tags.take(5).forEach { tag ->
                                Box(
                                    Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(tag, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
                TextButton(onClick = onDismiss, modifier = Modifier.align(Alignment.End)) {
                    Text("Close")
                }
            }
        }
    }
}