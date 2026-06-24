package shared.fake

import shared.domain.model.GeoCoordinates
import shared.domain.model.LocationData
import shared.domain.usecase.ResolveLocationUseCase

class FakeResolveLocationUseCase(
    private val result: Result<LocationData>
) : ResolveLocationUseCase {
    var calls = 0
    var lastCoordinates: GeoCoordinates? = null

    override suspend fun invoke(coordinates: GeoCoordinates): Result<LocationData> {
        calls++
        lastCoordinates = coordinates
        return result
    }
}
