package shared.fake

import shared.domain.model.AirQualityData
import shared.domain.model.GeoCoordinates
import shared.domain.repository.AirQualityRepository

class FakeAirQualityRepository(
    private val result: Result<AirQualityData>
) : AirQualityRepository {
    var calls = 0
    var lastCoordinates: GeoCoordinates? = null

    override suspend fun getAirQuality(coordinates: GeoCoordinates): Result<AirQualityData> {
        calls++
        lastCoordinates = coordinates
        return result
    }
}