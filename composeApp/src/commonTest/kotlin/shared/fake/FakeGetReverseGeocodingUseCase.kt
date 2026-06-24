package shared.fake

import shared.domain.model.GeoCoordinates
import shared.domain.model.LocationData
import shared.domain.usecase.GetReverseGeocodingUseCase

class FakeGetReverseGeocodingUseCase(
    private val result: Result<LocationData>
) : GetReverseGeocodingUseCase {
    var calls = 0
    var lastCoordinates: GeoCoordinates? = null

    override suspend fun invoke(coordinates: GeoCoordinates): Result<LocationData> {
        calls++
        lastCoordinates = coordinates
        return result
    }
}
