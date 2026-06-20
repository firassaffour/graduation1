package com.example.graduation1.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- Theme-aware palette ------------------------------------------------------
data class AppPalette(
    val red: Color, val tan: Color,
    val textPrimary: Color, val textSecondary: Color,
    val border: Color, val borderStrong: Color,
    val fieldFill: Color, val surface: Color, val background: Color,
    val bannerDark: Color, val codeBg: Color, val onlineGreen: Color,
    val chipBg: Color, val avatarBg: Color, val actionGray: Color,
)

val LightAppPalette = AppPalette(
    red = Color(0xFFE51E25), tan = Color(0xFFA67C52),
    textPrimary = Color(0xFF141414), textSecondary = Color(0xFF8C8C8C),
    border = Color(0xFFDADADA), borderStrong = Color(0xFF141414),
    fieldFill = Color(0xFFF0F0F0), surface = Color(0xFFFFFFFF), background = Color(0xFFFFFFFF),
    bannerDark = Color(0xFF2E3D3D), codeBg = Color(0xFF1E1E1E), onlineGreen = Color(0xFF2BD92B),
    chipBg = Color(0xFFF2F2F2), avatarBg = Color(0xFFE0E0E0), actionGray = Color(0xFF8C8C8C),
)

val DarkAppPalette = AppPalette(
    red = Color(0xFFE51E25), tan = Color(0xFFA67C52),          // brand held constant
    textPrimary = Color(0xFFF5F5F5), textSecondary = Color(0xFF9E9E9E),
    border = Color(0xFF333333), borderStrong = Color(0xFFF5F5F5),
    fieldFill = Color(0xFF2A2A2A), surface = Color(0xFF1E1E1E), background = Color(0xFF121212),
    bannerDark = Color(0xFF2E3D3D), codeBg = Color(0xFF0F0F0F), onlineGreen = Color(0xFF2BD92B),
    chipBg = Color(0xFF2A2A2A), avatarBg = Color(0xFF3A3A3A), actionGray = Color(0xFF6E6E6E),
)

val LocalAppColors = staticCompositionLocalOf { LightAppPalette }

// Drop-in accessor — same names as before, now light/dark aware.
object AppColors {
    val Red: Color          @Composable @ReadOnlyComposable get() = LocalAppColors.current.red
    val Tan: Color          @Composable @ReadOnlyComposable get() = LocalAppColors.current.tan
    val TextPrimary: Color  @Composable @ReadOnlyComposable get() = LocalAppColors.current.textPrimary
    val TextSecondary: Color @Composable @ReadOnlyComposable get() = LocalAppColors.current.textSecondary
    val Border: Color       @Composable @ReadOnlyComposable get() = LocalAppColors.current.border
    val BorderStrong: Color @Composable @ReadOnlyComposable get() = LocalAppColors.current.borderStrong
    val FieldFill: Color    @Composable @ReadOnlyComposable get() = LocalAppColors.current.fieldFill
    val Surface: Color      @Composable @ReadOnlyComposable get() = LocalAppColors.current.surface
    val Background: Color   @Composable @ReadOnlyComposable get() = LocalAppColors.current.background
    val BannerDark: Color   @Composable @ReadOnlyComposable get() = LocalAppColors.current.bannerDark
    val CodeBg: Color       @Composable @ReadOnlyComposable get() = LocalAppColors.current.codeBg
    val OnlineGreen: Color  @Composable @ReadOnlyComposable get() = LocalAppColors.current.onlineGreen
    val ChipBg: Color       @Composable @ReadOnlyComposable get() = LocalAppColors.current.chipBg
    val AvatarBg: Color     @Composable @ReadOnlyComposable get() = LocalAppColors.current.avatarBg
    val ActionGray: Color   @Composable @ReadOnlyComposable get() = LocalAppColors.current.actionGray
}

private fun schemeFrom(p: AppPalette, dark: Boolean) =
    if (dark) darkColorScheme(
        primary = p.red, onPrimary = Color.White,
        secondary = p.tan, onSecondary = Color.White,
        background = p.background, onBackground = p.textPrimary,
        surface = p.surface, onSurface = p.textPrimary, outline = p.border,
    ) else lightColorScheme(
        primary = p.red, onPrimary = Color.White,
        secondary = p.tan, onSecondary = Color.White,
        background = p.background, onBackground = p.textPrimary,
        surface = p.surface, onSurface = p.textPrimary, outline = p.border,
    )

// --- Type scale ---------------------------------------------------------------
private val Sans = FontFamily.SansSerif

val AppTypography = Typography(
    displaySmall = TextStyle(fontFamily = Sans, fontWeight = FontWeight.ExtraBold, fontSize = 32.sp, lineHeight = 38.sp),
    headlineSmall = TextStyle(fontFamily = Sans, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, lineHeight = 30.sp),
    titleLarge = TextStyle(fontFamily = Sans, fontWeight = FontWeight.Bold, fontSize = 20.sp, lineHeight = 26.sp),
    titleMedium = TextStyle(fontFamily = Sans, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 22.sp),
    bodyLarge = TextStyle(fontFamily = Sans, fontWeight = FontWeight.Normal, fontSize = 15.sp, lineHeight = 21.sp),
    bodyMedium = TextStyle(fontFamily = Sans, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp),
    labelLarge = TextStyle(fontFamily = Sans, fontWeight = FontWeight.SemiBold, fontSize = 14.sp),
    labelMedium = TextStyle(fontFamily = Sans, fontWeight = FontWeight.Medium, fontSize = 12.sp),
)

// --- Spacing ------------------------------------------------------------------
object Dimens {
    val screenPadding = 20.dp
    val itemSpacing = 16.dp
    val sectionSpacing = 28.dp
    val avatar = 64.dp
    val buttonHeight = 50.dp
    val fieldHeight = 56.dp
}

// --- Shapes -------------------------------------------------------------------
object Shapes {
    val pill = RoundedCornerShape(50)
    val action = RoundedCornerShape(10.dp)
    val card = RoundedCornerShape(12.dp)
    val field = RoundedCornerShape(28.dp)
    val chip = RoundedCornerShape(50)
}

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val palette = if (darkTheme) DarkAppPalette else LightAppPalette
    CompositionLocalProvider(LocalAppColors provides palette) {
        MaterialTheme(
            colorScheme = schemeFrom(palette, darkTheme),
            typography = AppTypography,
            content = content,
        )
    }
}
