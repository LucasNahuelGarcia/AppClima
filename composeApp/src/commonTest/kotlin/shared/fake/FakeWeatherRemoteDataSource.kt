package shared.fake

import shared.data.datasource.remote.WeatherRemoteDataSource
import shared.data.dto.OpenMeteoWeatherDto
import shared.domain.model.GeoCoordinates

class FakeWeatherRemoteDataSource(
    private val dtoToReturn: OpenMeteoWeatherDto? = null,
    private val exceptionToThrow: Exception? = null
) : WeatherRemoteDataSource {
    var calls = 0
    var lastCoordinates: GeoCoordinates? = null

    override suspend fun getWeather(coordinates: GeoCoordinates): OpenMeteoWeatherDto {
        calls++
        lastCoordinates = coordinates
        exceptionToThrow?.let { throw it }
        return dtoToReturn
            ?: throw IllegalStateException("Missing OpenMeteoWeatherDto")
    }
}
