package shared.domain.model

data class DashboardData(
    val weather: WeatherData,
    val moonPhase: MoonPhaseData,
    val airQuality: AirQualityData? = null
)
