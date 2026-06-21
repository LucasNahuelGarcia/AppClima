package shared.domain.usecase

import shared.domain.model.GeoCoordinates

interface GetDeviceLocationUseCase {
    suspend operator fun invoke(): Result<GeoCoordinates>
}
