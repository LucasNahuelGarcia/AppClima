package shared.data.datasource.remote

import shared.data.dto.OpenMeteoWeatherDto
import shared.domain.model.GeoCoordinates

interface WeatherRemoteDataSource {
    suspend fun getCurrentWeather(coordinates: GeoCoordinates): OpenMeteoWeatherDto
}
