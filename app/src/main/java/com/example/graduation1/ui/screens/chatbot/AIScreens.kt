package com.example.graduation1.ui.screens.chatbot

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.graduation1.ui.theme.AppColors
import com.example.graduation1.ui.theme.Dimens
import com.example.graduation1.ui.theme.Shapes
import com.example.graduation1.ui.components.*
import com.example.graduation1.ui.theme.AppTheme

/* ----------------------------- Mock model -------------------------------- */

data class Skill(val name: String, val level: Int)            // level 0..100
data class RecJob(val title: String, val company: String, val location: String, val match: Int, val reason: String)

private val mockSkills = listOf(
    Skill("Kotlin", 90), Skill("Jetpack Compose", 80), Skill("MVVM", 72),
    Skill("Coroutines", 55), Skill("Figma", 42), Skill("SQL", 35),
)
private val mockMatched = listOf("Kotlin", "Jetpack Compose", "MVVM", "REST APIs")
private val mockMissing = listOf("Coroutines", "Hilt", "Unit Testing")
private val mockBullets = listOf(
    "Built and shipped 6+ production screens in Jetpack Compose, improving release cadence.",
    "Refactored legacy views to an MVVM architecture, cutting crash rate by ~30%.",
    "Integrated REST APIs with Retrofit and managed UI state with StateFlow.",
)
private val mockRecJobs = listOf(
    RecJob("Android Developer", "Vortex Labs", "Cairo, Egypt", 88, "Matches Kotlin, Compose & MVVM"),
    RecJob("Mobile Engineer", "Nile Apps", "Giza, Egypt", 81, "Strong overlap with your coroutines work"),
    RecJob("Kotlin Developer", "Delta Soft", "Alexandria, Egypt", 74, "Good fit — consider adding Hilt"),
)

private fun levelLabel(level: Int) = when {
    level >= 80 -> "Advanced"
    level >= 50 -> "Intermediate"
    else -> "Beginner"
}

/* ----------------------- AI-specific local widgets ----------------------- */

@Composable
private fun MatchRing(progress: Float, size: Dp = 150.dp) {
    val track = AppColors.ChipBg
    val fg = AppColors.Red
    val pct = (progress.coerceIn(0f, 1f) * 100).toInt()
    Box(Modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val stroke = 14.dp.toPx()
            val inset = stroke / 2
            val arcSize = Size(this.size.width - stroke, this.size.height - stroke)
            val topLeft = Offset(inset, inset)
            drawArc(color = track, startAngle = -90f, sweepAngle = 360f, useCenter = false, topLeft = topLeft, size = arcSize, style = Stroke(stroke, cap = StrokeCap.Round))
            drawArc(color = fg, startAngle = -90f, sweepAngle = 360f * progress.coerceIn(0f, 1f), useCenter = false, topLeft = topLeft, size = arcSize, style = Stroke(stroke, cap = StrokeCap.Round))
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("$pct%", style = MaterialTheme.typography.displaySmall, color = AppColors.TextPrimary)
            Text("Match score", style = MaterialTheme.typography.bodyMedium, color = AppColors.TextSecondary)
        }
    }
}

@Composable
private fun StatusChip(text: String, matched: Boolean) {
    val bg = if (matched) AppColors.OnlineGreen.copy(alpha = 0.16f) else AppColors.ChipBg
    val fg = if (matched) AppColors.OnlineGreen else AppColors.TextSecondary
    Box(Modifier.clip(Shapes.chip).background(bg).padding(horizontal = 14.dp, vertical = 7.dp)) {
        Text(text, style = MaterialTheme.typography.labelLarge, color = fg)
    }
}

/* ------------------------- 1) Skill Dashboard ---------------------------- */

@Composable
fun SkillDashboardScreen(
    skills: List<Skill> = mockSkills,
    onBack: () -> Unit = {},
    onFindJobs: () -> Unit = {},
    navController: NavHostController
) {
    Scaffold(
        containerColor = AppColors.Background,
        bottomBar = { AppBottomBar(selected = NavTab.Profile, onSelect = {}, onFab = {}, navController) },
    ) { pad ->
        Column(Modifier.padding(pad).fillMaxSize().verticalScroll(rememberScrollState())) {
            AppTopBar(title = "Skill Dashboard", onBack = onBack)
            Column(Modifier.padding(horizontal = Dimens.screenPadding), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                AppCard {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Profile strength", style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary)
                        Text("78%", style = MaterialTheme.typography.headlineSmall, color = AppColors.Red)
                    }
                    Spacer(Modifier.height(12.dp))
                    ProgressBar(0.78f)
                    Spacer(Modifier.height(10.dp))
                    Text("Add 2 more in-demand skills to reach Strong.", style = MaterialTheme.typography.bodyMedium, color = AppColors.TextSecondary)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard("12", "Skills", Modifier.weight(1f))
                    StatCard("34", "Endorsements", Modifier.weight(1f))
                    StatCard("78%", "Match rate", Modifier.weight(1f))
                }
                SectionHeader("Your skills")
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    skills.forEach { s ->
                        Column {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text(s.name, style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary)
                                TagChip(levelLabel(s.level))
                            }
                            Spacer(Modifier.height(8.dp))
                            ProgressBar(s.level / 100f)
                        }
                    }
                }
                PrimaryButton("Find matching jobs", modifier = Modifier.fillMaxWidth(), onClick = onFindJobs)
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

/* -------------------------- 2) Match Results ----------------------------- */

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MatchResultsScreen(onBack: () -> Unit = {}, onViewJobs: () -> Unit = {}, navController: NavHostController) {
    Scaffold(
        containerColor = AppColors.Background,
        bottomBar = { AppBottomBar(selected = NavTab.Home, onSelect = {}, onFab = {}, navController) },
    ) { pad ->
        Column(Modifier.padding(pad).fillMaxSize().verticalScroll(rememberScrollState())) {
            AppTopBar(title = "Match Results", onBack = onBack)
            Column(Modifier.padding(horizontal = Dimens.screenPadding), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                AppCard {
                    Text("Android Developer", style = MaterialTheme.typography.titleLarge, color = AppColors.TextPrimary)
                    Text("Vortex Labs · Cairo, Egypt", style = MaterialTheme.typography.bodyLarge, color = AppColors.TextSecondary)
                }
                Box(Modifier.fillMaxWidth().padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                    MatchRing(0.82f)
                }
                SectionHeader("Matched skills")
                FlowRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    mockMatched.forEach { StatusChip(it, matched = true) }
                }
                SectionHeader("Missing skills")
                FlowRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    mockMissing.forEach { StatusChip(it, matched = false) }
                }
                AppCard {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(Icons.Outlined.Lightbulb, null, tint = AppColors.Red)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("Boost your match", style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary)
                            Spacer(Modifier.height(2.dp))
                            Text("Add Coroutines and Hilt to raise your match to ~92%.", style = MaterialTheme.typography.bodyMedium, color = AppColors.TextSecondary)
                        }
                    }
                }
                PrimaryButton("View matching jobs", modifier = Modifier.fillMaxWidth(), onClick = onViewJobs)
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

/* ----------------------- 3) Experience Generator ------------------------- */

@Composable
fun ExperienceGeneratorScreen(onBack: () -> Unit = {}) {
    var role by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var generated by remember { mutableStateOf(false) }

    // Fix: any edit to the inputs invalidates a previously generated result.
    Scaffold(containerColor = AppColors.Background) { pad ->
        Column(Modifier.padding(pad).fillMaxSize().verticalScroll(rememberScrollState())) {
            AppTopBar(title = "Experience Generator", onBack = onBack)
            Column(Modifier.padding(horizontal = Dimens.screenPadding), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Turn rough notes into polished resume bullet points.", style = MaterialTheme.typography.bodyLarge, color = AppColors.TextSecondary)
                AppTextField(role, { role = it; generated = false }, "Role / title")
                AppTextField(company, { company = it; generated = false }, "Company")
                AppTextField(duration, { duration = it; generated = false }, "Duration (e.g., 2022 – 2024)")
                AppTextField(notes, { notes = it; generated = false }, "What did you do? (rough notes)", singleLine = false, minLines = 4)
                PrimaryButton("Generate", modifier = Modifier.fillMaxWidth()) { generated = true }
                if (generated) {
                    AppCard {
                        Text("Generated experience", style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary)
                        Spacer(Modifier.height(12.dp))
                        mockBullets.forEach { line ->
                            Row(Modifier.padding(vertical = 4.dp)) {
                                Box(Modifier.padding(top = 8.dp).size(6.dp).clip(CircleShape).background(AppColors.Red))
                                Spacer(Modifier.width(10.dp))
                                Text(line, style = MaterialTheme.typography.bodyLarge, color = AppColors.TextPrimary)
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            ActionButton("Copy", filled = false, modifier = Modifier.weight(1f)) {}
                            ActionButton("Regenerate", filled = true, modifier = Modifier.weight(1f)) {}
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

/* ----------------------- 4) AI Recommended Jobs -------------------------- */

@Composable
fun AIRecommendedJobsScreen(
    jobs: List<RecJob> = mockRecJobs,
    onBack: () -> Unit = {},
    onViewDetails: (RecJob) -> Unit = {},
    onApply: (RecJob) -> Unit = {},
    navController: NavHostController
) {
    Scaffold(
        containerColor = AppColors.Background,
        bottomBar = { AppBottomBar(selected = NavTab.Home, onSelect = {}, onFab = {}, navController) },
    ) { pad ->
        Column(Modifier.padding(pad).fillMaxSize()) {
            AppTopBar(title = "Recommended for you", onBack = onBack)
            Text(
                "Based on your skills",
                style = MaterialTheme.typography.bodyLarge, color = AppColors.TextSecondary,
                modifier = Modifier.padding(horizontal = Dimens.screenPadding),
            )
            Spacer(Modifier.height(8.dp))
            LazyColumn(
                contentPadding = PaddingValues(Dimens.screenPadding),
                verticalArrangement = Arrangement.spacedBy(Dimens.itemSpacing),
            ) {
                items(jobs) { job -> RecJobCard(job, onViewDetails = { onViewDetails(job) }, onApply = { onApply(job) }) }
            }
        }
    }
}

@Composable
private fun RecJobCard(job: RecJob, onViewDetails: () -> Unit, onApply: () -> Unit) {
    Surface(shape = Shapes.card, color = AppColors.Surface, shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth().clickable(onClick = onViewDetails)) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Avatar(label = job.company, size = 52.dp)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(job.title, style = MaterialTheme.typography.titleLarge, color = AppColors.TextPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text("${job.company} · ${job.location}", style = MaterialTheme.typography.bodyLarge, color = AppColors.TextSecondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                MatchBadge(job.match)
            }
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.AutoAwesome, null, tint = AppColors.Red, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(job.reason, style = MaterialTheme.typography.bodyMedium, color = AppColors.TextSecondary)
            }
            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ActionButton("View details", filled = false, modifier = Modifier.weight(1f)) { onViewDetails() }
                PrimaryButton("Apply", modifier = Modifier.weight(1f)) { onApply() }
            }
        }
    }
}

/* ------------------------------ Previews --------------------------------- */

@Preview(showBackground = true, name = "5 · Skill Dashboard")
//@Composable private fun PreviewSkill() = AppTheme { SkillDashboardScreen() }

@Preview(showBackground = true, name = "6 · Match Results")
//@Composable private fun PreviewMatch() = AppTheme { MatchResultsScreen() }

@Preview(showBackground = true, name = "7 · Experience Generator")
@Composable private fun PreviewExp() = AppTheme { ExperienceGeneratorScreen() }

@Preview(showBackground = true, name = "8 · AI Recommended Jobs")
//@Composable private fun PreviewRec() = AppTheme { AIRecommendedJobsScreen() }

@Preview(showBackground = true, name = "5 · Skill Dashboard (Dark)")
//@Composable private fun PreviewSkillDark() = AppTheme(darkTheme = true) { SkillDashboardScreen() }

@Preview(showBackground = true, name = "6 · Match Results (Dark)")
//@Composable private fun PreviewMatchDark() = AppTheme(darkTheme = true) { MatchResultsScreen() }

@Preview(showBackground = true, name = "7 · Experience Generator (Dark)")
@Composable private fun PreviewExpDark() = AppTheme(darkTheme = true) { ExperienceGeneratorScreen() }

//@Preview(showBackground = true, name = "8 · AI Recommended Jobs (Dark)")
//@Composable private fun PreviewRecDark() = AppTheme(darkTheme = true) { AIRecommendedJobsScreen() }
