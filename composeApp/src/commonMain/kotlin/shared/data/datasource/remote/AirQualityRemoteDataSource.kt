package shared.data.datasource.remote

import shared.data.dto.OpenMeteoAirQualityDto
import shared.domain.model.GeoCoordinates

interface AirQualityRemoteDataSource {
    suspend fun getAirQuality(coordinates: GeoCoordinates): OpenMeteoAirQualityDto
}