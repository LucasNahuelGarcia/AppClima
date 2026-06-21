package shared.domain.repository

import shared.domain.model.GeoCoordinates
import shared.domain.model.LocationData

interface ReverseGeocodingRepository {
    suspend fun getLocation(coordinates: GeoCoordinates): Result<LocationData>
}
