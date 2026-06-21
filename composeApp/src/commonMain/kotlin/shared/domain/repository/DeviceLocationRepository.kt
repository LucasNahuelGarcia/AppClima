package shared.domain.repository

import shared.domain.model.GeoCoordinates

interface DeviceLocationRepository {
    suspend fun getCurrentCoordinates(): Result<GeoCoordinates>
}
