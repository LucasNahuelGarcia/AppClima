package shared.domain.usecase

import shared.domain.model.GeoCoordinates
import shared.domain.repository.DeviceLocationRepository

class GetDeviceLocationUseCaseImpl(
    private val deviceLocationRepository: DeviceLocationRepository
) : GetDeviceLocationUseCase {

    override suspend fun invoke(): Result<GeoCoordinates> {
        return deviceLocationRepository.getCurrentCoordinates()
    }
}
