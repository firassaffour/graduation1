package com.example.graduation1.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.graduation1.domain.models.AppPages
import com.example.graduation1.ui.theme.AppColors
import com.example.graduation1.ui.theme.Dimens
import com.example.graduation1.ui.theme.Shapes

/* ----------------------------- Buttons ----------------------------------- */

@Composable
fun PrimaryButton(text: String, modifier: Modifier = Modifier, enabled: Boolean = true, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = Shapes.pill,
        colors = ButtonDefaults.buttonColors(containerColor = AppColors.Red, contentColor = Color.White),
        contentPadding = PaddingValues(horizontal = 28.dp, vertical = 12.dp),
        modifier = modifier.heightIn(min = Dimens.buttonHeight),
    ) { Text(text, style = MaterialTheme.typography.titleMedium) }
}

@Composable
fun SecondaryButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = Shapes.pill,
        colors = ButtonDefaults.buttonColors(containerColor = AppColors.Tan, contentColor = Color.White),
        contentPadding = PaddingValues(horizontal = 28.dp, vertical = 12.dp),
        modifier = modifier.heightIn(min = Dimens.buttonHeight),
    ) { Text(text, style = MaterialTheme.typography.titleMedium) }
}

/** Profile-style action buttons (less rounded). filled=red, otherwise gray. */
@Composable
fun ActionButton(text: String, filled: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = Shapes.action,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (filled) AppColors.Red else AppColors.ActionGray,
            contentColor = Color.White,
        ),
        modifier = modifier.heightIn(min = Dimens.buttonHeight),
    ) { Text(text, style = MaterialTheme.typography.titleMedium) }
}

/* ------------------------------ Top bar ---------------------------------- */

@Composable
fun AppTopBar(title: String, onBack: (() -> Unit)? = null) {
    Column(Modifier.fillMaxWidth().padding(horizontal = Dimens.screenPadding)) {
        Spacer(Modifier.height(8.dp))
        if (onBack != null) {
            IconButton(onClick = onBack, modifier = Modifier.offset(x = (-12).dp)) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = AppColors.TextPrimary)
            }
        } else {
            Spacer(Modifier.height(8.dp))
        }
        Text(title, style = MaterialTheme.typography.displaySmall, color = AppColors.TextPrimary)
        Spacer(Modifier.height(8.dp))
    }
}

/* ---------------------------- Bottom nav --------------------------------- */

enum class NavTab { Home, People, Chat, Profile }

@Composable
fun AppBottomBar(selected: NavTab, onSelect: (NavTab) -> Unit, onFab: () -> Unit, navController: NavHostController) {
    Surface(shadowElevation = 8.dp, color = AppColors.Surface) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 12.dp),

            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(Modifier.weight(1f))
            NavIcon(Icons.Outlined.Home, selected == NavTab.Home) {
                onSelect(NavTab.Home)
            navController.navigate(AppPages.JobsList.route)}

            Spacer(Modifier.weight(1f))

            Box(
                Modifier.size(52.dp).clip(CircleShape).background(AppColors.Red).clickable {
                    navController.navigate(AppPages.OfferJob.route)
                    onFab() },
                contentAlignment = Alignment.Center,
            ) { Icon(Icons.Default.Add, contentDescription = "Create", tint = Color.White) }

            Spacer(Modifier.weight(1f))

            NavIcon(Icons.Outlined.AccountCircle, selected == NavTab.Profile) {
                onSelect(NavTab.Profile)
            navController.navigate(AppPages.AiProfile.route)}

            Spacer(Modifier.weight(1f))
        }
    }
}

@Composable
private fun NavIcon(icon: ImageVector, active: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(icon, contentDescription = null, tint = if (active) AppColors.Red else AppColors.TextPrimary, modifier = Modifier.size(28.dp))
    }
}

/* ------------------------------ Fields ----------------------------------- */

@Composable
fun SearchField(value: String, onValueChange: (String) -> Unit, placeholder: String = "Search", modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = AppColors.TextSecondary) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = AppColors.TextSecondary) },
        singleLine = true,
        shape = Shapes.field,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = AppColors.Border, focusedBorderColor = AppColors.TextSecondary,
            unfocusedContainerColor = AppColors.Surface, focusedContainerColor = AppColors.Surface,
        ),
        modifier = modifier.fillMaxWidth(),
    )
}

@Composable
fun AppTextField(
    value: String, onValueChange: (String) -> Unit, label: String,
    keyboardType: KeyboardType = KeyboardType.Text, singleLine: Boolean = true,
    minLines: Int = 1, modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Text(label, style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = value, onValueChange = onValueChange,
            singleLine = singleLine, minLines = minLines,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = if (singleLine) Shapes.field else Shapes.card,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = AppColors.Border, focusedBorderColor = AppColors.Red,
                unfocusedContainerColor = AppColors.Surface, focusedContainerColor = AppColors.Surface,
            ),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

/* ------------------------------ Avatar ----------------------------------- */

@Composable
fun Avatar(label: String, size: Dp = Dimens.avatar, online: Boolean = false, bg: Color? = null) {
    val resolved = bg ?: AppColors.AvatarBg
    Box(contentAlignment = Alignment.BottomEnd) {
        Box(Modifier.size(size).clip(CircleShape).background(resolved), contentAlignment = Alignment.Center) {
            Text(label.take(1).uppercase(), style = MaterialTheme.typography.titleLarge, color = AppColors.TextPrimary)
        }
        if (online) Box(Modifier.size(size * 0.28f).clip(CircleShape).background(AppColors.OnlineGreen).border(2.dp, AppColors.Surface, CircleShape))
    }
}

/* ------------------------------ Misc ------------------------------------- */

@Composable
fun SectionHeader(text: String, modifier: Modifier = Modifier) {
    Text(text, style = MaterialTheme.typography.headlineSmall, color = AppColors.TextPrimary, modifier = modifier)
}

@Composable
fun TagChip(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier.clip(Shapes.chip).background(AppColors.ChipBg).padding(horizontal = 14.dp, vertical = 7.dp),
    ) { Text(text, style = MaterialTheme.typography.labelLarge, color = AppColors.TextSecondary) }
}

@Composable
fun AppCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Surface(shape = Shapes.card, color = AppColors.Surface, shadowElevation = 2.dp, modifier = modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), content = content)
    }
}

/** Thin track + red fill. Shared by Skill bars, funnel, analytics. */
@Composable
fun ProgressBar(fraction: Float, modifier: Modifier = Modifier, height: Dp = 8.dp) {
    Box(modifier.fillMaxWidth().height(height).clip(Shapes.pill).background(AppColors.ChipBg)) {
        Box(Modifier.fillMaxWidth(fraction.coerceIn(0.01f, 1f)).height(height).clip(Shapes.pill).background(AppColors.Red))
    }
}

/** Bordered stat box matching the profile Post/Followers/Following cards. */
@Composable
fun StatCard(value: String, label: String, modifier: Modifier = Modifier) {
    Box(modifier.border(2.dp, AppColors.BorderStrong, Shapes.card).padding(vertical = 14.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, style = MaterialTheme.typography.titleLarge, color = AppColors.TextPrimary)
            Text(label, style = MaterialTheme.typography.bodyMedium, color = AppColors.TextSecondary)
        }
    }
}

/** Red pill match badge, e.g. "88% match". */
@Composable
fun MatchBadge(percent: Int, modifier: Modifier = Modifier) {
    Box(modifier.clip(Shapes.pill).background(AppColors.Red).padding(horizontal = 12.dp, vertical = 6.dp)) {
        Text("$percent% match", style = MaterialTheme.typography.labelLarge, color = Color.White)
    }
}

@Composable
fun LoadingState(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = AppColors.Red) }
}

@Composable
fun ErrorState(message: String = "Something went wrong. Pull to retry.", onRetry: (() -> Unit)? = null, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().padding(Dimens.screenPadding), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(Icons.Default.WifiOff, contentDescription = null, tint = AppColors.TextSecondary, modifier = Modifier.size(40.dp))
        Spacer(Modifier.height(12.dp))
        Text(message, style = MaterialTheme.typography.bodyLarge, color = AppColors.TextSecondary)
        if (onRetry != null) { Spacer(Modifier.height(16.dp)); PrimaryButton("Try again", onClick = onRetry) }
    }
}

@Composable
fun EmptyState(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().padding(Dimens.screenPadding), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(Icons.Outlined.Inbox, contentDescription = null, tint = AppColors.TextSecondary, modifier = Modifier.size(40.dp))
        Spacer(Modifier.height(12.dp))
        Text(title, style = MaterialTheme.typography.titleLarge, color = AppColors.TextPrimary)
        Spacer(Modifier.height(4.dp))
        Text(subtitle, style = MaterialTheme.typography.bodyLarge, color = AppColors.TextSecondary)
    }
}

@Composable
fun TrailingMeta(time: String, count: Int? = null) {
    Column(horizontalAlignment = Alignment.End) {
        Text(time, style = MaterialTheme.typography.labelLarge, color = AppColors.TextSecondary)
        if (count != null) {
            Spacer(Modifier.height(6.dp))
            Box(Modifier.size(22.dp).clip(CircleShape).background(AppColors.Red), contentAlignment = Alignment.Center) {
                Text(count.toString(), color = Color.White, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
fun RowEllipsis(text: String) = Text(text, maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodyLarge, color = AppColors.TextSecondary)
