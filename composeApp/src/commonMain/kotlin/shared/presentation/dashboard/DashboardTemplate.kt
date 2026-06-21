package shared.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dydsproject.composeapp.generated.resources.Res
import dydsproject.composeapp.generated.resources.dia_despejado
import dydsproject.composeapp.generated.resources.dia_lluvioso
import dydsproject.composeapp.generated.resources.dia_nublado
import dydsproject.composeapp.generated.resources.noche_despejado
import dydsproject.composeapp.generated.resources.noche_lluvioso
import dydsproject.composeapp.generated.resources.noche_nublado
import org.jetbrains.compose.resources.DrawableResource

@Composable
internal fun DashboardTemplate(
    uiState: DashboardUiState,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit
) {
    val presentation = (uiState as? DashboardUiState.Content)?.presentation
    val backgroundResource = dashboardBackgroundResource(presentation)

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val viewportHeight = maxHeight

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                DashboardHeroSection(
                    backgroundResource = backgroundResource,
                    viewportHeight = viewportHeight,
                    uiState = uiState,
                    presentation = presentation,
                    onRetry = onRetry
                )
            }

            item {
                if (presentation != null) {
                    DashboardDetailsSection(presentation)
                } else {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .background(MaterialTheme.colorScheme.background)
                    )
                }
            }
        }
    }
}

private fun dashboardBackgroundResource(
    presentation: DashboardPresentation?
): DrawableResource {
    if (presentation == null) {
        return Res.drawable.dia_despejado
    }

    return when (weatherBackgroundType(presentation.weather.weatherCode)) {
        WeatherBackgroundType.Clear -> if (presentation.themeMode == DashboardThemeMode.Night) {
            Res.drawable.noche_despejado
        } else {
            Res.drawable.dia_despejado
        }
        WeatherBackgroundType.Cloudy -> if (presentation.themeMode == DashboardThemeMode.Night) {
            Res.drawable.noche_nublado
        } else {
            Res.drawable.dia_nublado
        }
        WeatherBackgroundType.Rainy -> if (presentation.themeMode == DashboardThemeMode.Night) {
            Res.drawable.noche_lluvioso
        } else {
            Res.drawable.dia_lluvioso
        }
    }
}

private fun weatherBackgroundType(weatherCode: Int?): WeatherBackgroundType {
    return when (weatherCode) {
        0 -> WeatherBackgroundType.Clear
        1, 2, 3, 45, 48 -> WeatherBackgroundType.Cloudy
        in 51..67, in 71..77, in 80..82, in 85..86, in 95..99 -> WeatherBackgroundType.Rainy
        else -> WeatherBackgroundType.Cloudy
    }
}

private enum class WeatherBackgroundType {
    Clear,
    Cloudy,
    Rainy
}
