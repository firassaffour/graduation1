package com.example.graduation1.ui.screens.chatbot

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.graduation1.ui.theme.AppColors
import com.example.graduation1.ui.theme.Dimens
import com.example.graduation1.ui.theme.Shapes
import com.example.graduation1.ui.components.*
import com.example.graduation1.ui.theme.AppTheme

/* ----------------------------- Mock model -------------------------------- */

data class Job(
    val id: String,
    val title: String,
    val company: String,
    val location: String,
    val type: String,
    val mode: String,
    val salary: String,
    val posted: String,
    val description: String,
    val responsibilities: List<String>,
    val requirements: List<String>,
)

object MockJobs {
    val list = listOf(
        Job(
            "1", "Android Developer", "Vortex Labs", "Cairo, Egypt", "Full-time", "Remote",
            "EGP 35,000 – 50,000 / mo", "3 hours ago",
            "Build and ship features for our flagship social product used by thousands of developers. You'll own screens end to end in Jetpack Compose.",
            listOf("Develop UI in Jetpack Compose", "Collaborate with design on UX", "Write tests and review PRs"),
            listOf("2+ years Android / Kotlin", "Compose & MVVM experience", "Familiarity with Retrofit & coroutines"),
        ),
        Job(
            "2", "UI/UX Designer", "Pixel & Co", "Giza, Egypt", "Full-time", "Hybrid",
            "EGP 28,000 – 40,000 / mo", "5 hours ago",
            "Design clean, consistent interfaces and maintain our growing design system across mobile and web.",
            listOf("Own the design system", "Prototype new flows", "Hand off specs to engineering"),
            listOf("Strong Figma portfolio", "Mobile-first design sense", "Understanding of accessibility"),
        ),
        Job(
            "3", "Java Backend Engineer", "Java Bros Inc", "Alexandria, Egypt", "Contract", "On-site",
            "EGP 600 – 900 / hr", "12 days ago",
            "Maintain and scale backend services powering our community features.",
            listOf("Build REST APIs", "Optimize queries", "Monitor production services"),
            listOf("Spring Boot experience", "SQL fluency", "Comfortable on-call"),
        ),
    )
}

/* --------------------------- 1) Jobs Listing ----------------------------- */

@Composable
fun JobsListingScreen(
    jobs: List<Job> = MockJobs.list,
    onJobClick: (Job) -> Unit = {},
    onOfferJob: () -> Unit = {},
    onBack: () -> Unit = {},
    navController: NavHostController
) {
    var query by remember { mutableStateOf("") }
    val filters = listOf("All", "Remote", "Full-time", "Contract", "Design")
    var selectedFilter by remember { mutableStateOf("All") }

    Scaffold(
        containerColor = AppColors.Background,
        bottomBar = { AppBottomBar(selected = NavTab.Home, onSelect = {}, onFab = onOfferJob, navController) },
    ) { pad ->
        Column(Modifier.padding(pad).fillMaxSize()) {
            AppTopBar(title = "Jobs", onBack = onBack)
            Column(Modifier.padding(horizontal = Dimens.screenPadding)) {
                SearchField(value = query, onValueChange = { query = it }, placeholder = "Search jobs, companies…")
                Spacer(Modifier.height(14.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filters) { f -> FilterChipPill(text = f, selected = f == selectedFilter) { selectedFilter = f } }
                }
            }
            Spacer(Modifier.height(8.dp))
            LazyColumn(
                contentPadding = PaddingValues(Dimens.screenPadding),
                verticalArrangement = Arrangement.spacedBy(Dimens.itemSpacing),
            ) {
                items(jobs, key = { it.id }) { job -> JobCard(job) { onJobClick(job) } }
            }
        }
    }
}

@Composable
internal fun FilterChipPill(text: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        Modifier.clip(Shapes.chip)
            .background(if (selected) AppColors.Red else AppColors.ChipBg)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 9.dp),
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge, color = if (selected) androidx.compose.ui.graphics.Color.White else AppColors.TextSecondary)
    }
}

@Composable
private fun JobCard(job: Job, onClick: () -> Unit) {
    Surface(shape = Shapes.card, color = AppColors.Surface, shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Avatar(label = job.company, size = 52.dp)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(job.title, style = MaterialTheme.typography.titleLarge, color = AppColors.TextPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text("${job.company} · ${job.location}", style = MaterialTheme.typography.bodyLarge, color = AppColors.TextSecondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Text(job.posted, style = MaterialTheme.typography.labelLarge, color = AppColors.TextSecondary)
            }
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { TagChip(job.mode); TagChip(job.type) }
            Spacer(Modifier.height(12.dp))
            Text(job.salary, style = MaterialTheme.typography.titleMedium, color = AppColors.Red)
        }
    }
}

/* --------------------------- 2) Job Details ------------------------------ */

@Composable
fun JobDetailsScreen(job: Job = MockJobs.list.first(), onBack: () -> Unit = {}, onApply: () -> Unit = {}) {
    Scaffold(
        containerColor = AppColors.Background,
        bottomBar = {
            Surface(shadowElevation = 12.dp, color = AppColors.Surface) {
                Box(Modifier.fillMaxWidth().padding(Dimens.screenPadding)) {
                    PrimaryButton("Apply now", modifier = Modifier.fillMaxWidth(), onClick = onApply)
                }
            }
        },
    ) { pad ->
        Column(Modifier.padding(pad).fillMaxSize().verticalScroll(rememberScrollState())) {
            AppTopBar(title = "Job details", onBack = onBack)
            Column(Modifier.padding(horizontal = Dimens.screenPadding)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Avatar(label = job.company, size = 64.dp)
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text(job.title, style = MaterialTheme.typography.headlineSmall, color = AppColors.TextPrimary)
                        Text(job.company, style = MaterialTheme.typography.bodyLarge, color = AppColors.TextSecondary)
                    }
                }
                Spacer(Modifier.height(14.dp))
                IconLine(Icons.Outlined.LocationOn, job.location)
                IconLine(Icons.Outlined.Schedule, "${job.type} · ${job.mode}")
                IconLine(Icons.Outlined.Payments, job.salary)
                Spacer(Modifier.height(20.dp))
                DetailSection("About the role", job.description)
                BulletSection("Responsibilities", job.responsibilities)
                BulletSection("Requirements", job.requirements)
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun IconLine(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Icon(icon, null, tint = AppColors.TextSecondary, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge, color = AppColors.TextPrimary)
    }
}

@Composable
private fun DetailSection(title: String, body: String) {
    SectionHeader(title)
    Spacer(Modifier.height(8.dp))
    Text(body, style = MaterialTheme.typography.bodyLarge, color = AppColors.TextPrimary)
    Spacer(Modifier.height(20.dp))
}

@Composable
private fun BulletSection(title: String, items: List<String>) {
    SectionHeader(title)
    Spacer(Modifier.height(8.dp))
    items.forEach { line ->
        Row(Modifier.padding(vertical = 3.dp)) {
            Box(Modifier.padding(top = 8.dp).size(6.dp).clip(CircleShape).background(AppColors.Red))
            Spacer(Modifier.width(10.dp))
            Text(line, style = MaterialTheme.typography.bodyLarge, color = AppColors.TextPrimary)
        }
    }
    Spacer(Modifier.height(20.dp))
}

/* ---------------------------- 3) Apply Job ------------------------------- */

@Composable
fun ApplyJobScreen(job: Job = MockJobs.list.first(), onBack: () -> Unit = {}, onSubmit: () -> Unit = {}) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var cover by remember { mutableStateOf("") }
    var resumeAttached by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = AppColors.Background,
        bottomBar = {
            Surface(shadowElevation = 12.dp, color = AppColors.Surface) {
                Box(Modifier.fillMaxWidth().padding(Dimens.screenPadding)) {
                    PrimaryButton("Submit application", modifier = Modifier.fillMaxWidth(), onClick = onSubmit)
                }
            }
        },
    ) { pad ->
        Column(Modifier.padding(pad).fillMaxSize().verticalScroll(rememberScrollState())) {
            AppTopBar(title = "Apply", onBack = onBack)
            Column(Modifier.padding(horizontal = Dimens.screenPadding), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                AppCard {
                    Text(job.title, style = MaterialTheme.typography.titleLarge, color = AppColors.TextPrimary)
                    Text("${job.company} · ${job.location}", style = MaterialTheme.typography.bodyLarge, color = AppColors.TextSecondary)
                }
                AppTextField(name, { name = it }, "Full name")
                AppTextField(email, { email = it }, "Email", keyboardType = KeyboardType.Email)
                AppTextField(phone, { phone = it }, "Phone", keyboardType = KeyboardType.Phone)
                Column {
                    Text("Resume", style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary)
                    Spacer(Modifier.height(8.dp))
                    Surface(
                        shape = Shapes.card, color = AppColors.Surface,
                        modifier = Modifier.fillMaxWidth().border(1.dp, AppColors.Border, Shapes.card).clickable { resumeAttached = true },
                    ) {
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(if (resumeAttached) Icons.Default.CheckCircle else Icons.Outlined.UploadFile, null,
                                tint = if (resumeAttached) AppColors.Red else AppColors.TextSecondary)
                            Spacer(Modifier.width(12.dp))
                            Text(if (resumeAttached) "resume.pdf attached" else "Upload your resume (PDF)",
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (resumeAttached) AppColors.TextPrimary else AppColors.TextSecondary)
                        }
                    }
                }
                AppTextField(cover, { cover = it }, "Cover letter (optional)", singleLine = false, minLines = 4)
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

/* ---------------------------- 4) Offer Job ------------------------------- */

@Composable
fun OfferJobScreen(onBack: () -> Unit = {}, onPost: () -> Unit = {}) {
    var title by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var salary by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val types = listOf("Full-time", "Part-time", "Contract")
    val modes = listOf("Remote", "On-site", "Hybrid")
    var type by remember { mutableStateOf(types.first()) }
    var mode by remember { mutableStateOf(modes.first()) }

    Scaffold(
        containerColor = AppColors.Background,
        bottomBar = {
            Surface(shadowElevation = 12.dp, color = AppColors.Surface) {
                Box(Modifier.fillMaxWidth().padding(Dimens.screenPadding)) {
                    PrimaryButton("Post job", modifier = Modifier.fillMaxWidth(), onClick = onPost)
                }
            }
        },
    ) { pad ->
        Column(Modifier.padding(pad).fillMaxSize().verticalScroll(rememberScrollState())) {
            AppTopBar(title = "Offer a job", onBack = onBack)
            Column(Modifier.padding(horizontal = Dimens.screenPadding), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                AppTextField(title, { title = it }, "Job title")
                AppTextField(company, { company = it }, "Company")
                AppTextField(location, { location = it }, "Location")
                ChipGroup("Employment type", types, type) { type = it }
                ChipGroup("Work mode", modes, mode) { mode = it }
                AppTextField(salary, { salary = it }, "Salary range")
                AppTextField(description, { description = it }, "Description", singleLine = false, minLines = 5)
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun ChipGroup(label: String, options: List<String>, selected: String, onSelect: (String) -> Unit) {
    Column {
        Text(label, style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            options.forEach { opt -> FilterChipPill(text = opt, selected = opt == selected) { onSelect(opt) } }
        }
    }
}

/* ------------------------------ Previews --------------------------------- */

@Preview(showBackground = true, name = "1 · Jobs Listing")
//@Composable private fun PreviewListing() = AppTheme { JobsListingScreen() }

@Preview(showBackground = true, name = "2 · Job Details")
@Composable private fun PreviewDetails() = AppTheme { JobDetailsScreen() }

@Preview(showBackground = true, name = "3 · Apply Job")
@Composable private fun PreviewApply() = AppTheme { ApplyJobScreen() }

@Preview(showBackground = true, name = "4 · Offer Job")
@Composable private fun PreviewOffer() = AppTheme { OfferJobScreen() }

//@Preview(showBackground = true, name = "1 · Jobs Listing (Dark)")
//@Composable private fun PreviewListingDark() = AppTheme(darkTheme = true) { JobsListingScreen() }
