package shared.presentation.dashboard

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DashboardDayScheme = lightColorScheme(
    primary = Color(0xFF557A3E),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD8E7C8),
    onPrimaryContainer = Color(0xFF2B4020),

    secondary = Color(0xFF937666),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF5E6DC),
    onSecondaryContainer = Color(0xFF3D2C24),

    tertiary = Color(0xFF0F7AC6),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD0E8FF),
    onTertiaryContainer = Color(0xFF001D36),
    background = Color(0xFFEBF5DF),
    onBackground = Color(0xFF001D36),
    surface = Color(0xFFF4F8EC),
    onSurface = Color(0xFF001D36),
    surfaceVariant = Color(0xFFE0E4D8),
    onSurfaceVariant = Color(0xFF424940),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002)
)

private val DashboardNightScheme = darkColorScheme(
    primary = Color(0xFF91BB77),
    onPrimary = Color(0xFF1A2B10),
    primaryContainer = Color(0xFF3B4E2E),
    onPrimaryContainer = Color(0xFFEBF5DF),
    secondary = Color(0xFFD4BBAE),
    onSecondary = Color(0xFF2B1E18),
    secondaryContainer = Color(0xFF5C4338),
    onSecondaryContainer = Color(0xFFF5E6DC),
    tertiary = Color(0xFF20A4F3),
    onTertiary = Color(0xFF001D36),
    tertiaryContainer = Color(0xFF004A77),
    onTertiaryContainer = Color(0xFFD0E8FF),
    background = Color(0xFF121212),
    onBackground = Color(0xFFEBF5DF),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFEBF5DF),
    surfaceVariant = Color(0xFF3D3A4B),
    onSurfaceVariant = Color(0xFFE0E4D8),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6)
)
@Composable
internal fun DashboardTheme(
    themeMode: DashboardThemeMode,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (themeMode == DashboardThemeMode.Night) {
            DashboardNightScheme
        } else {
            DashboardDayScheme
        },
        content = content
    )
}
