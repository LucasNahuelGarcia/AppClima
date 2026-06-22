package shared.domain.repository

import shared.domain.model.AirQualityData
import shared.domain.model.GeoCoordinates

interface AirQualityRepository {
    suspend fun getAirQuality(coordinates: GeoCoordinates): Result<AirQualityData>
}