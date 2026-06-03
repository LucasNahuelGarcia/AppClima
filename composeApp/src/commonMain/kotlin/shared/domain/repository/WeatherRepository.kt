package shared.domain.repository

import shared.domain.model.GeoCoordinates
import shared.domain.model.WeatherData

interface WeatherRepository {
    suspend fun getCurrentWeather(coordinates: GeoCoordinates): Result<WeatherData>
}
