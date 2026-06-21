package shared.fake

import shared.domain.model.GeoCoordinates
import shared.domain.model.LocationData
import shared.domain.repository.ReverseGeocodingRepository

class FakeReverseGeocodingRepository(
    private val result: Result<LocationData>
) : ReverseGeocodingRepository {
    var calls = 0
    var lastCoordinates: GeoCoordinates? = null

    override suspend fun getLocation(coordinates: GeoCoordinates): Result<LocationData> {
        calls++
        lastCoordinates = coordinates
        return result
    }
}
