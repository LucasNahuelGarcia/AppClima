package shared.domain.model

data class WeatherData(
    val coordinates: GeoCoordinates,
    val temperatureCelsius: Double,
    val windSpeedKmh: Double,
    val weatherCode: Int,
    val timeIso: String
)
