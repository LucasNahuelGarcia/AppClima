package shared.domain.usecase

import shared.domain.model.GeoCoordinates
import shared.domain.model.LocationData

interface ResolveLocationUseCase {
    suspend operator fun invoke(coordinates: GeoCoordinates): Result<LocationData>
}
