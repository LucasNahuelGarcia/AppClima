package shared.domain.repository

import shared.domain.model.GeoCoordinates

interface ApproximateDeviceLocationRepository {
    suspend fun getApproximateCoordinates(): Result<GeoCoordinates>
}
