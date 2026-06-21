package shared.data.mapper

import shared.data.dto.OpenMeteoWeatherDto
import shared.domain.model.GeoCoordinates
import shared.domain.model.WeatherData
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

fun OpenMeteoWeatherDto.toDomainModel(nowProvider: () -> Instant = { Clock.System.now() }): WeatherData {
    return WeatherData(
        coordinates = GeoCoordinates(latitude = latitude, longitude = longitude),
        temperatureCelsius = current.temperatureCelsius,
        windSpeedKmh = current.windSpeedKmh,
        weatherCode = current.weatherCode,
    )
}
