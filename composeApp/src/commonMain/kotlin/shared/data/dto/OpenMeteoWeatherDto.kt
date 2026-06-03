package shared.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenMeteoWeatherDto(
    val latitude: Double,
    val longitude: Double,
    @SerialName("current") val current: OpenMeteoCurrentDto
)

@Serializable
data class OpenMeteoCurrentDto(
    @SerialName("time") val time: String,
    @SerialName("temperature_2m") val temperatureCelsius: Double,
    @SerialName("wind_speed_10m") val windSpeedKmh: Double,
    @SerialName("weather_code") val weatherCode: Int
)
