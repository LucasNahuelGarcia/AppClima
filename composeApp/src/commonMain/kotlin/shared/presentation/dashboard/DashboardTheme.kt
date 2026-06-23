package shared.presentation.dashboard

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import shared.domain.model.DayNight

private val DashboardDayScheme = lightColorScheme(
    primary = Color(0xFF557A3E),
    onPrimary = Color(0xFFEBF5DF),
    primaryContainer = Color(0xFFD8E7C8),
    onPrimaryContainer = Color(0xFF2B4020),

    secondary = Color(0xFF667460),
    onSecondary = Color(0xFFC5E2C0),
    secondaryContainer = Color(0xFF6B7265),
    onSecondaryContainer = Color(0xFFE5F3D8),

    tertiary = Color(0xFF557A3E),
    onTertiary = Color(0xFFEBF5DF),
    tertiaryContainer = Color(0xFFD8E7C8),
    onTertiaryContainer = Color(0xFF2B4020),
    background = Color(0xFFEBF5DF),
    onBackground = Color(0xFF424B54),
    surface = Color(0xFFF4F7EF),
    onSurface = Color(0xFF424B54),
    surfaceVariant = Color(0xFFDCE4D1),
    onSurfaceVariant = Color(0xFF5B6457),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002)
)

private val DashboardNightScheme = darkColorScheme(
    primary = Color(0xFF88CB6A),
    onPrimary = Color(0xFF0B1005),
    primaryContainer = Color(0xFF2D431E),
    onPrimaryContainer = Color(0xFFE8EBED),
    secondary = Color(0xFFE8EBED),
    onSecondary = Color(0xFF0B1005),
    secondaryContainer = Color(0xFF20261A),
    onSecondaryContainer = Color(0xFFE8EBED),
    tertiary = Color(0xFF557A3E),
    onTertiary = Color(0xFF0B1005),
    tertiaryContainer = Color(0xFF2D431E),
    onTertiaryContainer = Color(0xFFE8EBED),
    background = Color(0xFF0B1005),
    onBackground = Color(0xFFE8EBED),
    surface = Color(0xFF12170C),
    onSurface = Color(0xFFE8EBED),
    surfaceVariant = Color(0xFF20271A),
    onSurfaceVariant = Color(0xFFC2C7C9),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFFFDAD6)
)
@Composable
internal fun DashboardTheme(
    themeMode: DayNight,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (themeMode == DayNight.Night) {
            DashboardNightScheme
        } else {
            DashboardDayScheme
        },
        content = content
    )
}
