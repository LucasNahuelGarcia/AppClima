package shared.data.datasource.remote

import shared.data.dto.NominatimReverseGeocodingDto
import shared.domain.model.GeoCoordinates

interface ReverseGeocodingRemoteDataSource {
    suspend fun getLocation(coordinates: GeoCoordinates): NominatimReverseGeocodingDto
}
