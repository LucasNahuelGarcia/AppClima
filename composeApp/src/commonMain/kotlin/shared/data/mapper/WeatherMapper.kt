package shared.data.mapper

import kotlin.math.roundToInt
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import shared.data.dto.OpenMeteoHourlyDto
import shared.data.dto.OpenMeteoWeatherDto
import shared.domain.model.DayNight
import shared.domain.model.GeoCoordinates
import shared.domain.model.HourlyForecast
import shared.domain.model.WeatherCondition
import shared.domain.model.WeatherData

fun OpenMeteoWeatherDto.toDomainModel(nowProvider: () -> Instant = { Clock.System.now() }): WeatherData {
    val now = nowProvider()
    val timeZone = weatherTimeZone()
    return WeatherData(
        coordinates = GeoCoordinates(latitude = latitude, longitude = longitude),
        temperatureCelsius = current.temperatureCelsius,
        windSpeedKmh = current.windSpeedKmh,
        condition = current.weatherCode.toWeatherCondition(),
        dayNight = current.isDay.toDayNight(),
        hourlyForecast = hourly.toDomainModel(
            now = now,
            timeZone = timeZone
        )
    )
}

private fun OpenMeteoWeatherDto.weatherTimeZone(): TimeZone {
    return timezone
        ?.let { runCatching { TimeZone.of(it) }.getOrNull() }
        ?: TimeZone.UTC
}

private fun OpenMeteoHourlyDto.toDomainModel(
    now: Instant,
    timeZone: TimeZone
): List<HourlyForecast> {
    val currentDateTime = now.toLocalDateTime(timeZone)
    val size = minOf(time.size, temperatureCelsius.size, weatherCode.size)

    return (0 until size).mapNotNull { index ->
        val hourlyDateTime = time[index].toLocalDateTimeOrNull(timeZone) ?: return@mapNotNull null
        if (hourlyDateTime.date != currentDateTime.date || hourlyDateTime.hour < currentDateTime.hour) {
            return@mapNotNull null
        }

        HourlyForecast(
            time = hourlyDateTime.hour.toString().padStart(2, '0') + ":00",
            temperatureCelsius = temperatureCelsius[index].roundToInt(),
            condition = weatherCode[index].toWeatherCondition()
        )
    }
}

private fun String.toLocalDateTimeOrNull(timeZone: TimeZone) =
    runCatching { Instant.parse(this).toLocalDateTime(timeZone) }
        .getOrElse {
            runCatching { kotlinx.datetime.LocalDateTime.parse(this) }.getOrNull()
        }
