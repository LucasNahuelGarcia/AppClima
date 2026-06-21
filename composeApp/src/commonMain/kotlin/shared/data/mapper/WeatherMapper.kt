package shared.data.mapper

import shared.data.dto.OpenMeteoWeatherDto
import shared.domain.model.GeoCoordinates
import shared.domain.model.WeatherData

fun OpenMeteoWeatherDto.toDomainModel(): WeatherData {
    return WeatherData(
        coordinates = GeoCoordinates(latitude = latitude, longitude = longitude),
        temperatureCelsius = current.temperatureCelsius,
        windSpeedKmh = current.windSpeedKmh,
        weatherCode = current.weatherCode,
        timeIso = current.time,
        formattedDate = current.time
    )
}
