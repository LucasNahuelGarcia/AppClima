package shared.data.mapper

import kotlin.math.roundToInt
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import shared.data.dto.OpenMeteoHourlyDto
import shared.data.dto.OpenMeteoWeatherDto
import shared.domain.model.GeoCoordinates
import shared.domain.model.HourlyForecast
import shared.domain.model.WeatherData

fun OpenMeteoWeatherDto.toDomainModel(nowProvider: () -> Instant = { Clock.System.now() }): WeatherData {
    val now = nowProvider()
    return WeatherData(
        coordinates = GeoCoordinates(latitude = latitude, longitude = longitude),
        temperatureCelsius = current.temperatureCelsius,
        windSpeedKmh = current.windSpeedKmh,
        weatherCode = current.weatherCode,
        hourlyForecast = hourly.toDomainModel(now)
    )
}

private fun OpenMeteoHourlyDto.toDomainModel(now: Instant): List<HourlyForecast> {
    val currentDateTime = now.toLocalDateTime(TimeZone.UTC)
    val size = minOf(time.size, temperatureCelsius.size, weatherCode.size)

    return (0 until size).mapNotNull { index ->
        val hourlyDateTime = time[index].toUtcLocalDateTimeOrNull() ?: return@mapNotNull null
        if (hourlyDateTime.date != currentDateTime.date || hourlyDateTime.hour < currentDateTime.hour) {
            return@mapNotNull null
        }

        HourlyForecast(
            time = hourlyDateTime.hour.toString().padStart(2, '0') + ":00",
            temperatureCelsius = temperatureCelsius[index].roundToInt(),
            weatherCode = weatherCode[index]
        )
    }
}

private fun String.toUtcLocalDateTimeOrNull() =
    runCatching { Instant.parse(this).toLocalDateTime(TimeZone.UTC) }
        .getOrElse {
            runCatching { kotlinx.datetime.LocalDateTime.parse(this) }.getOrNull()
        }
