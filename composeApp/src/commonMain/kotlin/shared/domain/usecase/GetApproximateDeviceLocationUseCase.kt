package shared.domain.usecase

import shared.domain.model.GeoCoordinates

interface GetApproximateDeviceLocationUseCase {
    suspend operator fun invoke(): Result<GeoCoordinates>
}
