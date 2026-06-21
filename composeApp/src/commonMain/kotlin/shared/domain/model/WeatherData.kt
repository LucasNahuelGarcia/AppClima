package shared.domain.model

data class WeatherData(
    val coordinates: GeoCoordinates,
    val temperatureCelsius: Double,
    val windSpeedKmh: Double,
    val weatherCode: Int,
    val locationName: String = "${coordinates.latitude}, ${coordinates.longitude}",
    val weatherAnimUrl: String = "",
    val hourlyForecast: List<HourlyForecast> = emptyList(),
    val isNight: Boolean = false
)
