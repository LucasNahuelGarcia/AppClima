package shared.domain.usecase

import shared.domain.model.GeoCoordinates
import shared.domain.model.LocationData

interface GetReverseGeocodingUseCase {
    suspend operator fun invoke(coordinates: GeoCoordinates): Result<LocationData>
}
