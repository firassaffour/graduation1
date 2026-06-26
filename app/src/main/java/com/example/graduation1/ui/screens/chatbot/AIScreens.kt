package com.example.graduation1.ui.screens.chatbot

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.graduation1.domain.models.requets_response.JobResponse
import com.example.graduation1.ui.theme.AppColors
import com.example.graduation1.ui.theme.Dimens
import com.example.graduation1.ui.theme.Shapes
import com.example.graduation1.ui.components.*
import com.example.graduation1.ui.theme.AppTheme
import com.example.graduation1.viewmodel.ChatbotViewModel

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

/* ═══════════════════════════════════════════════════════════════════════
   1. EXPERIENCE GENERATOR  (wired to API)
   ═══════════════════════════════════════════════════════════════════════ */

@Composable
fun ExperienceGeneratorScreen(
    chatbotViewModel: ChatbotViewModel,
    onBack: () -> Unit = {}
) {
    val experienceResult by chatbotViewModel.experienceResult.collectAsState()
    val isGenerating     by chatbotViewModel.isGeneratingExp.collectAsState()
    val error            by chatbotViewModel.error.collectAsState()

    var role by remember { mutableStateOf("") }
    val clipboard = LocalClipboardManager.current

    LaunchedEffect(Unit) { chatbotViewModel.clearExperience() }

    Scaffold(containerColor = AppColors.Background) { pad ->
        Column(Modifier.padding(pad).fillMaxSize().verticalScroll(rememberScrollState())) {
            AppTopBar(title = "Experience Generator", onBack = onBack)
            Column(
                Modifier.padding(horizontal = Dimens.screenPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Enter your target role and the AI will write polished CV bullet points for you.",
                    style = MaterialTheme.typography.bodyLarge, color = AppColors.TextSecondary
                )

                AppTextField(
                    value = role,
                    onValueChange = { role = it; chatbotViewModel.clearExperience() },
                    label = "Target role (e.g. Android Developer)",

                )

                PrimaryButton(
                    text = if (isGenerating) "Generating…" else "Generate",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { chatbotViewModel.generateExperience(role.ifBlank { null }) }
                )

                // Error
                if (error != null) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        shape = Shapes.card, modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(error ?: "", color = MaterialTheme.colorScheme.onErrorContainer, modifier = Modifier.weight(1f))
                            TextButton(onClick = { chatbotViewModel.clearError() }) { Text("Dismiss") }
                        }
                    }
                }

                // Loading spinner
                if (isGenerating) {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = AppColors.Red)
                    }
                }

                // Result card
                AnimatedVisibility(
                    visible = experienceResult != null,
                    enter = fadeIn() + slideInVertically { it / 2 }
                ) {
                    val result = experienceResult ?: return@AnimatedVisibility
                    AppCard {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Generated experience", style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary)
                            IconButton(onClick = {
                                val text = result.cvBullets.joinToString("\n") { "• $it" }
                                clipboard.setText(AnnotatedString(text))
                            }) {
                                Icon(Icons.Outlined.ContentCopy, "copy", tint = AppColors.TextSecondary)
                            }
                        }

                        if (!result.roleUnderstanding.isNullOrBlank()) {
                            Spacer(Modifier.height(8.dp))
                            Text(result.roleUnderstanding, style = MaterialTheme.typography.bodyMedium, color = AppColors.TextSecondary)
                        }

                        Spacer(Modifier.height(12.dp))
                        result.cvBullets.forEach { line ->
                            Row(Modifier.padding(vertical = 4.dp)) {
                                Box(Modifier.padding(top = 8.dp).size(6.dp).clip(CircleShape).background(AppColors.Red))
                                Spacer(Modifier.width(10.dp))
                                Text(line, style = MaterialTheme.typography.bodyLarge, color = AppColors.TextPrimary)
                            }
                        }

                        Spacer(Modifier.height(12.dp))
                        ActionButton("Regenerate", filled = true, modifier = Modifier.fillMaxWidth()) {
                            chatbotViewModel.generateExperience(role.ifBlank { null })
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

/* ═══════════════════════════════════════════════════════════════════════
   2. MATCH RESULTS  (wired to real job match API)
   ═══════════════════════════════════════════════════════════════════════ */

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MatchResultsScreen(
    chatbotViewModel: ChatbotViewModel,
    onBack: () -> Unit = {},
    onViewJobs: () -> Unit = {},
    navController: NavHostController
) {
    val selectedJob by chatbotViewModel.selectedJob.collectAsState()
    val jobMatch    by chatbotViewModel.jobMatch.collectAsState()

    Scaffold(
        containerColor = AppColors.Background,
        bottomBar = { AppBottomBar(selected = NavTab.Home, onSelect = {}, onFab = {}, navController) }
    ) { pad ->
        Column(Modifier.padding(pad).fillMaxSize().verticalScroll(rememberScrollState())) {
            AppTopBar(title = "Match Results", onBack = onBack)

            if (selectedJob == null || jobMatch == null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Select a job to see your match.", color = AppColors.TextSecondary)
                }
                return@Scaffold
            }

            val job   = selectedJob!!
            val match = jobMatch!!

            Column(Modifier.padding(horizontal = Dimens.screenPadding), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                AppCard {
                    Text(job.title, style = MaterialTheme.typography.titleLarge, color = AppColors.TextPrimary)
                    Text(job.location ?: "", style = MaterialTheme.typography.bodyLarge, color = AppColors.TextSecondary)
                }

                Box(Modifier.fillMaxWidth().padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                    MatchRing(match.matchPercentage / 100f)
                }

                SectionHeader("Matched skills")
                FlowRow(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) { match.strongSkills.forEach { StatusChip(it, matched = true) } }

                SectionHeader("Missing skills")
                FlowRow(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) { match.missingSkills.forEach { StatusChip(it, matched = false) } }

                if (match.missingSkills.isNotEmpty()) {
                    AppCard {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(Icons.Outlined.Lightbulb, null, tint = AppColors.Red)
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text("Boost your match", style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary)
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    "Add ${match.missingSkills.take(2).joinToString(" and ")} to raise your match score.",
                                    style = MaterialTheme.typography.bodyMedium, color = AppColors.TextSecondary
                                )
                            }
                        }
                    }
                }

                PrimaryButton("View matching jobs", modifier = Modifier.fillMaxWidth(), onClick = onViewJobs)
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

/* ═══════════════════════════════════════════════════════════════════════
   3. JOBS LISTING  (wired to real API)
   ═══════════════════════════════════════════════════════════════════════ */

@Composable
fun JobsListingScreen(
    chatbotViewModel: ChatbotViewModel,
    onJobClick: (JobResponse) -> Unit = {},
    onOfferJob: () -> Unit = {},
    onBack: () -> Unit = {},
    navController: NavHostController
) {
    val jobs       by chatbotViewModel.jobs.collectAsState()
    val isLoading  by chatbotViewModel.isLoadingJobs.collectAsState()

    var query          by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Remote", "On-site", "Hybrid", "Full-time", "Contract")

    // Filter locally by mode/type chips; search hits the API
    val filtered = remember(jobs, query, selectedFilter) {
        jobs.filter { job ->
            val matchesSearch = query.isBlank() ||
                    job.title.contains(query, ignoreCase = true) ||
                    job.location?.contains(query, ignoreCase = true) == true
            val matchesFilter = selectedFilter == "All" ||
                    job.experienceLevel?.contains(selectedFilter, ignoreCase = true) == true ||
                    job.requiredSkillsJson?.contains(selectedFilter, ignoreCase = true) == true
            matchesSearch && matchesFilter
        }
    }

    Scaffold(
        containerColor = AppColors.Background,
        bottomBar = { AppBottomBar(selected = NavTab.Home, onSelect = {}, onFab = onOfferJob, navController) }
    ) { pad ->
        Column(Modifier.padding(pad).fillMaxSize()) {
            AppTopBar(title = "Jobs", onBack = onBack)
            Column(Modifier.padding(horizontal = Dimens.screenPadding)) {
                SearchField(
                    value = query,
                    onValueChange = {
                        query = it
                        if (it.length >= 2) chatbotViewModel.searchJobs(q = it)
                    },
                    placeholder = "Search jobs, titles…"
                )
                Spacer(Modifier.height(14.dp))
                androidx.compose.foundation.lazy.LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filters) { f ->
                        FilterChipPill(text = f, selected = f == selectedFilter) { selectedFilter = f }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))

            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AppColors.Red)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(Dimens.screenPadding),
                    verticalArrangement = Arrangement.spacedBy(Dimens.itemSpacing)
                ) {
                    items(filtered, key = { it.jobID }) { job ->
                        RealJobCard(job) { onJobClick(job) }
                    }
                }
            }
        }
    }
}

@Composable
private fun RealJobCard(job: JobResponse, onClick: () -> Unit) {
    Surface(
        shape = Shapes.card, color = AppColors.Surface, shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Avatar(label = job.title, size = 52.dp)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(job.title, style = MaterialTheme.typography.titleLarge, color = AppColors.TextPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(job.location ?: "Remote", style = MaterialTheme.typography.bodyLarge, color = AppColors.TextSecondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                if (!job.isOpen) TagChip("Closed")
            }
            Spacer(Modifier.height(12.dp))
            if (!job.experienceLevel.isNullOrBlank()) TagChip(job.experienceLevel)
        }
    }
}

/* ═══════════════════════════════════════════════════════════════════════
   4. JOB DETAILS  (wired to real API)
   ═══════════════════════════════════════════════════════════════════════ */

@Composable
fun JobDetailsScreen(
    chatbotViewModel: ChatbotViewModel,
    onBack: () -> Unit = {},
    onApply: () -> Unit = {}
) {
    val job      by chatbotViewModel.selectedJob.collectAsState()
    val match    by chatbotViewModel.jobMatch.collectAsState()
    val hasApplied = job?.let { chatbotViewModel.hasApplied(it.jobID) } == true

    if (job == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = AppColors.Red)
        }
        return
    }

    val j = job!!

    Scaffold(
        containerColor = AppColors.Background,
        bottomBar = {
            Surface(shadowElevation = 12.dp, color = AppColors.Surface) {
                Box(Modifier.fillMaxWidth().padding(Dimens.screenPadding)) {
                    PrimaryButton(
                        text = if (hasApplied) "Already Applied ✓" else "Apply now",
                        modifier = Modifier.fillMaxWidth(),
                        onClick = if (hasApplied) {{}} else onApply
                    )
                }
            }
        }
    ) { pad ->
        Column(Modifier.padding(pad).fillMaxSize().verticalScroll(rememberScrollState())) {
            AppTopBar(title = "Job details", onBack = onBack)
            Column(Modifier.padding(horizontal = Dimens.screenPadding)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Avatar(label = j.title, size = 64.dp)
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text(j.title, style = MaterialTheme.typography.headlineSmall, color = AppColors.TextPrimary)
                        Text(j.location ?: "Remote", style = MaterialTheme.typography.bodyLarge, color = AppColors.TextSecondary)
                    }
                }
                Spacer(Modifier.height(14.dp))
                if (!j.experienceLevel.isNullOrBlank()) {
                    Row(Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.WorkOutline, null, tint = AppColors.TextSecondary, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(j.experienceLevel, style = MaterialTheme.typography.bodyLarge, color = AppColors.TextPrimary)
                    }
                }

                // Match score if available
                if (match != null) {
                    Spacer(Modifier.height(16.dp))
                    AppCard {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            MatchRing(match!!.matchPercentage / 100f, size = 80.dp)
                            Spacer(Modifier.width(16.dp))
                            Column {
                                Text("Your match score", style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary)
                                Spacer(Modifier.height(4.dp))
                                Text("${match!!.strongSkills.size} skills matched", style = MaterialTheme.typography.bodyMedium, color = AppColors.TextSecondary)
                                if (match!!.missingSkills.isNotEmpty())
                                    Text("Missing: ${match!!.missingSkills.take(2).joinToString(", ")}", style = MaterialTheme.typography.bodySmall, color = AppColors.TextSecondary)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))
                if (!j.description.isNullOrBlank()) {
                    SectionHeader("About the role")
                    Spacer(Modifier.height(8.dp))
                    Text(j.description, style = MaterialTheme.typography.bodyLarge, color = AppColors.TextPrimary)
                    Spacer(Modifier.height(20.dp))
                }

                // Parse required skills from JSON string
                val skills = remember(j.requiredSkillsJson) {
                    try {
                        j.requiredSkillsJson
                            ?.removePrefix("[")?.removeSuffix("]")
                            ?.split(",")
                            ?.map { it.trim().removePrefix("\"").removeSuffix("\"") }
                            ?.filter { it.isNotBlank() }
                            ?: emptyList()
                    } catch (e: Exception) { emptyList() }
                }
                if (skills.isNotEmpty()) {
                    SectionHeader("Required skills")
                    Spacer(Modifier.height(8.dp))
                    skills.forEach { skill ->
                        Row(Modifier.padding(vertical = 3.dp)) {
                            Box(Modifier.padding(top = 8.dp).size(6.dp).clip(CircleShape).background(AppColors.Red))
                            Spacer(Modifier.width(10.dp))
                            Text(skill, style = MaterialTheme.typography.bodyLarge, color = AppColors.TextPrimary)
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

/* ═══════════════════════════════════════════════════════════════════════
   5. APPLY JOB  (wired to real API)
   ═══════════════════════════════════════════════════════════════════════ */

@Composable
fun ApplyJobScreen(
    chatbotViewModel: ChatbotViewModel,
    onBack: () -> Unit = {},
    onSubmitSuccess: () -> Unit = {}
) {
    val job        by chatbotViewModel.selectedJob.collectAsState()
    val isApplying by chatbotViewModel.isApplying.collectAsState()
    val success    by chatbotViewModel.applySuccess.collectAsState()
    val error      by chatbotViewModel.error.collectAsState()

    var cover by remember { mutableStateOf("") }

    // Navigate away when apply succeeds
    LaunchedEffect(success) {
        if (success) { chatbotViewModel.resetApplySuccess(); onSubmitSuccess() }
    }

    if (job == null) return

    val j = job!!

    Scaffold(
        containerColor = AppColors.Background,
        bottomBar = {
            Surface(shadowElevation = 12.dp, color = AppColors.Surface) {
                Box(Modifier.fillMaxWidth().padding(Dimens.screenPadding)) {
                    PrimaryButton(
                        text = if (isApplying) "Submitting…" else "Submit application",
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { chatbotViewModel.applyToJob(j.jobID, cover) }
                    )
                }
            }
        }
    ) { pad ->
        Column(Modifier.padding(pad).fillMaxSize().verticalScroll(rememberScrollState())) {
            AppTopBar(title = "Apply", onBack = onBack)
            Column(Modifier.padding(horizontal = Dimens.screenPadding), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                AppCard {
                    Text(j.title, style = MaterialTheme.typography.titleLarge, color = AppColors.TextPrimary)
                    Text(j.location ?: "Remote", style = MaterialTheme.typography.bodyLarge, color = AppColors.TextSecondary)
                }

                if (error != null) {
                    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer), shape = Shapes.card) {
                        Text(error ?: "", Modifier.padding(12.dp), color = MaterialTheme.colorScheme.onErrorContainer)
                    }
                }

                AppTextField(cover, { cover = it }, "Cover letter (optional)", singleLine = false, minLines = 4)

                if (isApplying) {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = AppColors.Red)
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

/* ═══════════════════════════════════════════════════════════════════════
   6. OFFER JOB  (wired to real API)
   ═══════════════════════════════════════════════════════════════════════ */

@Composable
fun OfferJobScreen(
    chatbotViewModel: ChatbotViewModel,
    onBack: () -> Unit = {},
    onPostSuccess: () -> Unit = {}
) {
    val isLoading by chatbotViewModel.isLoadingJobs.collectAsState()
    val error     by chatbotViewModel.error.collectAsState()

    var title       by remember { mutableStateOf("") }
    var location    by remember { mutableStateOf("") }
    var expLevel    by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var skillsText  by remember { mutableStateOf("") }  // comma-separated

    val expOptions = listOf("Junior", "Mid", "Senior", "Lead")
    var selectedExp by remember { mutableStateOf("") }

    Scaffold(
        containerColor = AppColors.Background,
        bottomBar = {
            Surface(shadowElevation = 12.dp, color = AppColors.Surface) {
                Box(Modifier.fillMaxWidth().padding(Dimens.screenPadding)) {
                    PrimaryButton(
                        text = if (isLoading) "Posting…" else "Post job",
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            if (title.isBlank()) return@PrimaryButton
                            val skills = skillsText.split(",").map { it.trim() }.filter { it.isNotBlank() }
                            chatbotViewModel.createJob(title, description, location, selectedExp.ifBlank { expLevel }, skills) {
                                onPostSuccess()
                            }
                        }
                    )
                }
            }
        }
    ) { pad ->
        Column(Modifier.padding(pad).fillMaxSize().verticalScroll(rememberScrollState())) {
            AppTopBar(title = "Offer a job", onBack = onBack)
            Column(Modifier.padding(horizontal = Dimens.screenPadding), verticalArrangement = Arrangement.spacedBy(16.dp)) {

                if (error != null) {
                    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer), shape = Shapes.card) {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(error ?: "", Modifier.weight(1f), color = MaterialTheme.colorScheme.onErrorContainer)
                            TextButton(onClick = { chatbotViewModel.clearError() }) { Text("Dismiss") }
                        }
                    }
                }

                AppTextField(title, { title = it }, "Job title *")
                AppTextField(location, { location = it }, "Location")

                Column {
                    Text("Experience level", style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary)
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        expOptions.forEach { opt ->
                            FilterChipPill(opt, opt == selectedExp) { selectedExp = opt }
                        }
                    }
                }

                AppTextField(skillsText, { skillsText = it }, "Required skills (comma-separated)")
                AppTextField(description, { description = it }, "Description", singleLine = false, minLines = 5)
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

/* ═══════════════════════════════════════════════════════════════════════
   7. CANDIDATE RANKING  (wired to real API)
   ═══════════════════════════════════════════════════════════════════════ */

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CandidateRankingScreen(
    chatbotViewModel: ChatbotViewModel,
    onBack: () -> Unit = {},
    navController: NavHostController
) {
    val jobs         by chatbotViewModel.jobs.collectAsState()
    val jobCandidates by chatbotViewModel.jobCandidates.collectAsState()
    val isLoading    by chatbotViewModel.isLoadingJobs.collectAsState()

    var selectedJobId by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        containerColor = AppColors.Background,
        bottomBar = { AppBottomBar(selected = NavTab.Profile, onSelect = {}, onFab = {}, navController) }
    ) { pad ->
        Column(Modifier.padding(pad).fillMaxSize()) {
            AppTopBar(title = "Candidate Ranking", onBack = onBack)
            Column(Modifier.padding(horizontal = Dimens.screenPadding)) {

                // Job picker
                Text("Select a job to rank candidates", style = MaterialTheme.typography.bodyMedium, color = AppColors.TextSecondary)
                Spacer(Modifier.height(8.dp))
                androidx.compose.foundation.lazy.LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(jobs) { job ->
                        FilterChipPill(job.title, job.jobID == selectedJobId) {
                            selectedJobId = job.jobID
                            chatbotViewModel.loadJobCandidates(job.jobID)
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AppColors.Red)
                }
            } else if (jobCandidates == null || selectedJobId == null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Select a job above to see ranked candidates.", color = AppColors.TextSecondary)
                }
            } else {
                val dash = jobCandidates!!
                Column(Modifier.padding(horizontal = Dimens.screenPadding)) {
                    AppCard {
                        Text("Ranked for", style = MaterialTheme.typography.bodyMedium, color = AppColors.TextSecondary)
                        Text(dash.jobTitle ?: "Job #${dash.jobID}", style = MaterialTheme.typography.titleLarge, color = AppColors.TextPrimary)
                        Text("${dash.totalCandidates} candidates", style = MaterialTheme.typography.bodyMedium, color = AppColors.TextSecondary)
                    }
                }
                Spacer(Modifier.height(8.dp))
                LazyColumn(
                    contentPadding = PaddingValues(Dimens.screenPadding),
                    verticalArrangement = Arrangement.spacedBy(Dimens.itemSpacing)
                ) {
                    items(dash.candidates.sortedByDescending { it.overallScore }) { c ->
                        Surface(shape = Shapes.card, color = AppColors.Surface, shadowElevation = 2.dp, modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // rank number
                                    Box(Modifier.size(34.dp).clip(CircleShape).background(AppColors.ChipBg), contentAlignment = Alignment.Center) {
                                        Text(
                                            (dash.candidates.sortedByDescending { it.overallScore }.indexOf(c) + 1).toString(),
                                            style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary
                                        )
                                    }
                                    Spacer(Modifier.width(12.dp))
                                    Avatar(label = c.displayName, size = 52.dp)
                                    Spacer(Modifier.width(12.dp))
                                    Column(Modifier.weight(1f)) {
                                        Text(c.displayName, style = MaterialTheme.typography.titleLarge, color = AppColors.TextPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                        Text("${c.preferredRole ?: ""} · ${c.country ?: ""}", style = MaterialTheme.typography.bodyMedium, color = AppColors.TextSecondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    }
                                    MatchBadge(c.matchPercentage)
                                }
                                if (c.strongSkills.isNotEmpty()) {
                                    Spacer(Modifier.height(12.dp))
                                    FlowRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        c.strongSkills.take(4).forEach { TagChip(it) }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/* ═══════════════════════════════════════════════════════════════════════
   8. RECRUITER ANALYTICS  (unchanged — uses local mock)
   ═══════════════════════════════════════════════════════════════════════ */

private val mockApplications = listOf("W1" to 12f, "W2" to 18f, "W3" to 9f, "W4" to 22f, "W5" to 16f, "W6" to 25f)
private val mockFunnel       = listOf("Applied" to 128, "Screened" to 64, "Interview" to 24, "Offer" to 6, "Hired" to 3)
private val mockTopSkills    = listOf("Kotlin" to 0.72f, "Jetpack Compose" to 0.58f, "MVVM" to 0.49f, "Coroutines" to 0.37f)

@Composable
fun RecruiterAnalyticsDashboardScreen(
    chatbotViewModel: ChatbotViewModel,
    onBack: () -> Unit = {},
    navController: NavHostController
) {
    val jobs = chatbotViewModel.jobs.collectAsState().value
    val openCount   = jobs.count { it.isOpen }
    val closedCount = jobs.count { !it.isOpen }

    Scaffold(
        containerColor = AppColors.Background,
        bottomBar = { AppBottomBar(selected = NavTab.Profile, onSelect = {}, onFab = {}, navController) }
    ) { pad ->
        Column(Modifier.padding(pad).fillMaxSize().verticalScroll(rememberScrollState())) {
            AppTopBar(title = "Analytics", onBack = onBack)
            Column(Modifier.padding(horizontal = Dimens.screenPadding), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(openCount.toString(),   "Open jobs",    Modifier.weight(1f))
                    StatCard(closedCount.toString(), "Closed jobs",  Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(mockFunnel.first().second.toString(), "Total applicants", Modifier.weight(1f))
                    StatCard(mockFunnel.last().second.toString(),  "Hired",           Modifier.weight(1f))
                }
                SectionHeader("Applications (last 6 weeks)")
                AppCard {
                    BarChartWidget(mockApplications)
                }
                SectionHeader("Hiring funnel")
                AppCard {
                    val maxCount = mockFunnel.maxOf { it.second }.toFloat()
                    mockFunnel.forEachIndexed { i, (stage, count) ->
                        if (i > 0) Spacer(Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(stage, style = MaterialTheme.typography.bodyMedium, color = AppColors.TextPrimary, modifier = Modifier.width(80.dp))
                            ProgressBar(count / maxCount, modifier = Modifier.weight(1f), height = 12.dp)
                            Spacer(Modifier.width(12.dp))
                            Text(count.toString(), style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary, modifier = Modifier.width(40.dp))
                        }
                    }
                }
                SectionHeader("Top candidate skills")
                AppCard {
                    mockTopSkills.forEachIndexed { i, (skill, frac) ->
                        if (i > 0) Spacer(Modifier.height(14.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(skill, style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary)
                            Text("${(frac * 100).toInt()}%", style = MaterialTheme.typography.bodyMedium, color = AppColors.TextSecondary)
                        }
                        Spacer(Modifier.height(8.dp))
                        ProgressBar(frac)
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

/** Simple bar chart extracted from RecruiterScreens */
@Composable
private fun BarChartWidget(data: List<Pair<String, Float>>) {
    val maxV = (data.maxOfOrNull { it.second } ?: 1f).coerceAtLeast(1f)
    Row(Modifier.fillMaxWidth().height(160.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.Bottom) {
        data.forEach { (label, v) ->
            Column(Modifier.weight(1f).fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.BottomCenter) {
                    Box(
                        Modifier.fillMaxWidth(0.6f)
                            .fillMaxHeight((v / maxV).coerceIn(0.02f, 1f))
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                            .background(AppColors.Red)
                    )
                }
                Spacer(Modifier.height(6.dp))
                Text(label, style = MaterialTheme.typography.labelMedium, color = AppColors.TextSecondary)
            }
        }
    }
}


/* ------------------------------ Previews --------------------------------- */


