package shared.presentation.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.runtime.Composable
import shared.domain.model.WeatherData

@Composable
internal fun DashboardHeroContent(
    weather: WeatherData,
    locationName: String
) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn()
    ) {
        DashboardHeroWeatherCard(
            weather = weather,
            locationName = locationName
        )
    }
}
