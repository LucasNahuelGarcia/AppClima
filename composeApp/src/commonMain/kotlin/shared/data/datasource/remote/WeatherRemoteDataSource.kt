package shared.data.datasource.remote

import shared.data.dto.OpenMeteoWeatherDto
import shared.domain.model.GeoCoordinates

interface WeatherRemoteDataSource {
    suspend fun getWeather(coordinates: GeoCoordinates): OpenMeteoWeatherDto
}
