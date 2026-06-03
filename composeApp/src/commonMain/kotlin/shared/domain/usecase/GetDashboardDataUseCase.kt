package shared.domain.usecase

import shared.domain.model.DashboardData
import shared.domain.model.GeoCoordinates

interface GetDashboardDataUseCase {
    suspend operator fun invoke(coordinates: GeoCoordinates): Result<DashboardData>
}
