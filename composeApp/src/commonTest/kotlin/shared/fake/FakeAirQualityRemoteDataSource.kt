package shared.fake

import shared.data.datasource.remote.AirQualityRemoteDataSource
import shared.data.dto.OpenMeteoAirQualityDto
import shared.domain.model.GeoCoordinates

class FakeAirQualityRemoteDataSource(
    private val dtoToReturn: OpenMeteoAirQualityDto? = null,
    private val exceptionToThrow: Exception? = null
) : AirQualityRemoteDataSource {
    var calls = 0
    var lastCoordinates: GeoCoordinates? = null

    override suspend fun getAirQuality(coordinates: GeoCoordinates): OpenMeteoAirQualityDto {
        calls++
        lastCoordinates = coordinates
        exceptionToThrow?.let { throw it }
        return dtoToReturn
            ?: throw IllegalStateException("Missing OpenMeteoAirQualityDto")
    }
}