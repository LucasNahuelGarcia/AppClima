package shared.fake

import shared.domain.model.GeoCoordinates
import shared.domain.model.WeatherData
import shared.domain.repository.WeatherRepository

class FakeWeatherRepository(
    private val result: Result<WeatherData>
) : WeatherRepository {
    var calls = 0
    var lastCoordinates: GeoCoordinates? = null

    override suspend fun getCurrentWeather(coordinates: GeoCoordinates): Result<WeatherData> {
        calls++
        lastCoordinates = coordinates
        return result
    }
}
