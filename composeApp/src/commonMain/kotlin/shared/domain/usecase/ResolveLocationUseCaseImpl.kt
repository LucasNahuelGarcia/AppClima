package shared.domain.usecase

import shared.domain.model.GeoCoordinates
import shared.domain.model.LocationData
import shared.domain.repository.ReverseGeocodingRepository

class ResolveLocationUseCaseImpl(
    private val reverseGeocodingRepository: ReverseGeocodingRepository
) : ResolveLocationUseCase {

    override suspend fun invoke(coordinates: GeoCoordinates): Result<LocationData> {
        return reverseGeocodingRepository.getLocation(coordinates)
    }
}
