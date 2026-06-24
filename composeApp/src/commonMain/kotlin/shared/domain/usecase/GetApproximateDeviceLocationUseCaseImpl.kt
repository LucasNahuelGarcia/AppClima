package shared.domain.usecase

import shared.domain.model.GeoCoordinates
import shared.domain.repository.ApproximateDeviceLocationRepository

class GetApproximateDeviceLocationUseCaseImpl(
    private val approximateDeviceLocationRepository: ApproximateDeviceLocationRepository
) : GetApproximateDeviceLocationUseCase {

    override suspend fun invoke(): Result<GeoCoordinates> {
        return approximateDeviceLocationRepository.getApproximateCoordinates()
    }
}
