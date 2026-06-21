package shared.presentation.dashboard

import shared.domain.model.DashboardData
import shared.domain.model.DayNight
import shared.domain.model.HourlyForecast
import shared.domain.model.LocationData
import shared.domain.model.MoonPhaseData
import shared.domain.model.WeatherData
import shared.presentation.state.UiState

internal sealed interface DashboardUiState {
    data object Loading : DashboardUiState

    data class Error(
        val message: String
    ) : DashboardUiState

    data class Content(
        val presentation: DashboardPresentation
    ) : DashboardUiState
}

internal data class DashboardPresentation(
    val weather: WeatherData,
    val locationName: String,
    val hourlyForecast: List<HourlyForecast>,
    val moonPhase: MoonPhaseData
)

internal fun UiState<DashboardData>.toDashboardUiState(
    locationState: UiState<LocationData>
): DashboardUiState {
    return when (this) {
        UiState.Loading -> DashboardUiState.Loading
        is UiState.Error -> DashboardUiState.Error(message = message)
        is UiState.Success -> {
            val weather = data.weather
            DashboardUiState.Content(
                presentation = DashboardPresentation(
                    weather = weather,
                    locationName = locationState.locationNameOrFallback(weather.locationName),
                    hourlyForecast = weather.hourlyForecast,
                    moonPhase = data.astronomy.moonPhase
                )
            )
        }
    }
}

private fun UiState<LocationData>.locationNameOrFallback(fallback: String): String {
    return when (this) {
        UiState.Loading -> "Resolviendo ubicación"
        is UiState.Error -> fallback
        is UiState.Success -> data.displayName.ifBlank { fallback }
    }
        .substringBefore(",")
        .trim()
}
