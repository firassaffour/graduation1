package com.example.graduation1.ui.screens.chatbot

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.graduation1.R
import com.example.graduation1.language
import com.example.graduation1.ui.theme.AppColors
import com.example.graduation1.ui.theme.Dimens
import com.example.graduation1.ui.theme.Shapes
import com.example.graduation1.ui.components.*
import com.example.graduation1.ui.theme.AppTheme

/* ----------------------------- Mock model -------------------------------- */

data class Candidate(
    val rank: Int, val name: String, val title: String,
    val match: Int, val skills: List<String>, val location: String,
)

private val mockCandidates = listOf(
    Candidate(1, "Sara Mostafa", "Android Developer", 92, listOf("Kotlin", "Compose", "MVVM"), "Cairo, Egypt"),
    Candidate(2, "Omar Khaled", "Mobile Engineer", 88, listOf("Kotlin", "Coroutines", "Hilt"), "Giza, Egypt"),
    Candidate(3, "Mariam Adel", "Android Developer", 81, listOf("Java", "Compose", "REST"), "Alexandria, Egypt"),
    Candidate(4, "Youssef Hany", "Kotlin Developer", 76, listOf("Kotlin", "SQL"), "Mansoura, Egypt"),
    Candidate(5, "Nourhan Samir", "Junior Android Dev", 68, listOf("Kotlin", "XML"), "Cairo, Egypt"),
)

private val mockApplications = listOf("W1" to 12f, "W2" to 18f, "W3" to 9f, "W4" to 22f, "W5" to 16f, "W6" to 25f)
private val mockFunnel = listOf("Applied" to 128, "Screened" to 64, "Interview" to 24, "Offer" to 6, "Hired" to 3)
private val mockTopSkills = listOf("Kotlin" to 0.72f, "Jetpack Compose" to 0.58f, "MVVM" to 0.49f, "Coroutines" to 0.37f)

/* --------------------------- Local widgets ------------------------------- */

@Composable
private fun SelectChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        Modifier.clip(Shapes.chip)
            .background(if (selected) AppColors.Red else AppColors.ChipBg)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 9.dp),
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge, color = if (selected) Color.White else AppColors.TextSecondary)
    }
}

@Composable
private fun RankBadge(rank: Int) {
    Box(Modifier.size(34.dp).clip(CircleShape).background(AppColors.ChipBg), contentAlignment = Alignment.Center) {
        Text(rank.toString(), style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary)
    }
}

/** Simple proportional bar chart. */
@Composable
private fun BarChart(data: List<Pair<String, Float>>, modifier: Modifier = Modifier) {
    val maxV = (data.maxOfOrNull { it.second } ?: 1f).coerceAtLeast(1f)
    val barColor = AppColors.Red
    val labelColor = AppColors.TextSecondary
    Row(modifier.fillMaxWidth().height(160.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.Bottom) {
        data.forEach { (label, v) ->
            Column(Modifier.weight(1f).fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.BottomCenter) {
                    Box(
                        Modifier.fillMaxWidth(0.6f)
                            .fillMaxHeight((v / maxV).coerceIn(0.02f, 1f))
                            .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                            .background(barColor),
                    )
                }
                Spacer(Modifier.height(6.dp))
                Text(label, style = MaterialTheme.typography.labelMedium, color = labelColor)
            }
        }
    }
}

/* ----------------------- 1) Candidate Ranking ---------------------------- */

@Composable
fun CandidateRankingScreen(
    candidates: List<Candidate> = mockCandidates,
    onBack: () -> Unit = {},
    onViewCandidate: (Candidate) -> Unit = {},
    navController: NavHostController
) {
    val sorts = listOf("Best match", "Newest", "Experience")
    var sort by remember { mutableStateOf(sorts.first()) }

    Scaffold(
        containerColor = AppColors.Background,
        bottomBar = { AppBottomBar(selected = NavTab.Profile, onSelect = {}, onFab = {}, navController) },
    ) { pad ->
        Column(Modifier.padding(pad).fillMaxSize()) {
            AppTopBar(title = "Candidate Ranking", onBack = onBack)
            Column(Modifier.padding(horizontal = Dimens.screenPadding)) {
                AppCard {
                    Text("Ranked for", style = MaterialTheme.typography.bodyMedium, color = AppColors.TextSecondary)
                    Text("Android Developer · Vortex Labs", style = MaterialTheme.typography.titleLarge, color = AppColors.TextPrimary)
                }
                Spacer(Modifier.height(14.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    sorts.forEach { s -> SelectChip(s, s == sort) { sort = s } }
                }
            }
            Spacer(Modifier.height(8.dp))
            LazyColumn(
                contentPadding = PaddingValues(Dimens.screenPadding),
                verticalArrangement = Arrangement.spacedBy(Dimens.itemSpacing),
            ) {
                items(candidates, key = { it.rank }) { c -> CandidateCard(c) { onViewCandidate(c) } }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CandidateCard(c: Candidate, onClick: () -> Unit) {
    Surface(shape = Shapes.card, color = AppColors.Surface, shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RankBadge(c.rank)
                Spacer(Modifier.width(12.dp))
                Avatar(label = c.name, size = 52.dp)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(c.name, style = MaterialTheme.typography.titleLarge, color = AppColors.TextPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text("${c.title} · ${c.location}", style = MaterialTheme.typography.bodyMedium, color = AppColors.TextSecondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                MatchBadge(c.match)
            }
            Spacer(Modifier.height(12.dp))
            FlowRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                c.skills.forEach { TagChip(it) }
            }
        }
    }
}

/* ------------------- 2) Recruiter Analytics Dashboard -------------------- */

@Composable
fun RecruiterAnalyticsDashboardScreen(onBack: () -> Unit = {}, navController: NavHostController) {
    Scaffold(
        containerColor = AppColors.Background,
        bottomBar = { AppBottomBar(selected = NavTab.Profile, onSelect = {}, onFab = {}, navController) },
    ) { pad ->
        Column(Modifier.padding(pad).fillMaxSize().verticalScroll(rememberScrollState())) {
            AppTopBar(title = "Analytics", onBack = onBack)
            Column(Modifier.padding(horizontal = Dimens.screenPadding), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard("128", "Applicants", Modifier.weight(1f))
                    StatCard("24", "Interviews", Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard("6", "Offers", Modifier.weight(1f))
                    StatCard("79%", "Avg match", Modifier.weight(1f))
                }

                SectionHeader("Applications (last 6 weeks)")
                AppCard { BarChart(mockApplications) }

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

@Composable
fun AiProfileScreen(onCandidate: () -> Unit, onAnalytics: () -> Unit, onSkillDashboard: () -> Unit, onMatchResults: () -> Unit, onExperienceGenerator: () -> Unit, navController: NavHostController){

    Scaffold(
        containerColor = AppColors.Background,
        bottomBar = { AppBottomBar(selected = NavTab.Profile, onSelect = {}, onFab = {}, navController) },
    ) { pad ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(pad)
        ) {

            Spacer(Modifier.weight(1f))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onCandidate() }) {

                Spacer(Modifier.width(20.dp))

                Text(
                    text = "Candidate Ranking",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.weight(1f))

                Icon(
                    painter = painterResource(id = R.drawable.rightarrow),
                    contentDescription = "right arrow",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(30.dp)
                        .rotate(if (language == "ar") 180f else 0f)
                )
            } // Row

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onAnalytics() }) {

                Spacer(Modifier.width(20.dp))

                Text(
                    text = "Analytics",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.weight(1f))

                Icon(
                    painter = painterResource(id = R.drawable.rightarrow),
                    contentDescription = "right arrow",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(30.dp)
                        .rotate(if (language == "ar") 180f else 0f)
                )
            } // Row

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onSkillDashboard() }) {

                Spacer(Modifier.width(20.dp))

                Text(
                    text = "Skill Dashboard",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.weight(1f))

                Icon(
                    painter = painterResource(id = R.drawable.rightarrow),
                    contentDescription = "right arrow",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(30.dp)
                        .rotate(if (language == "ar") 180f else 0f)
                )
            } // Row

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onMatchResults() }) {

                Spacer(Modifier.width(20.dp))

                Text(
                    text = "Matching Results",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.weight(1f))

                Icon(
                    painter = painterResource(id = R.drawable.rightarrow),
                    contentDescription = "right arrow",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(30.dp)
                        .rotate(if (language == "ar") 180f else 0f)
                )
            } // Row

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onExperienceGenerator() }) {

                Spacer(Modifier.width(20.dp))

                Text(
                    text = "Experience Generator",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.weight(1f))

                Icon(
                    painter = painterResource(id = R.drawable.rightarrow),
                    contentDescription = "right arrow",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(30.dp)
                        .rotate(if (language == "ar") 180f else 0f)
                )
            } // Row

            Spacer(Modifier.weight(1f))
        }
    }
}

/* ------------------------------ Previews --------------------------------- */

//@Preview(showBackground = true, name = "9 · Candidate Ranking")
//@Composable private fun PreviewRanking() = AppTheme { CandidateRankingScreen() }

//@Preview(showBackground = true, name = "10 · Recruiter Analytics")
//@Composable private fun PreviewAnalytics() = AppTheme { RecruiterAnalyticsDashboardScreen() }

//@Preview(showBackground = true, name = "9 · Candidate Ranking (Dark)")
//@Composable private fun PreviewRankingDark() = AppTheme(darkTheme = true) { CandidateRankingScreen() }

//@Preview(showBackground = true, name = "10 · Recruiter Analytics (Dark)")
//@Composable private fun PreviewAnalyticsDark() = AppTheme(darkTheme = true) { RecruiterAnalyticsDashboardScreen() }
