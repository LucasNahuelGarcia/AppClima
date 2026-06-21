package shared.fake

import shared.data.datasource.remote.ReverseGeocodingRemoteDataSource
import shared.data.dto.NominatimReverseGeocodingDto
import shared.domain.model.GeoCoordinates

class FakeReverseGeocodingRemoteDataSource(
    private val dtoToReturn: NominatimReverseGeocodingDto? = null,
    private val exceptionToThrow: Exception? = null
) : ReverseGeocodingRemoteDataSource {
    var calls = 0
    var lastCoordinates: GeoCoordinates? = null

    override suspend fun getLocation(coordinates: GeoCoordinates): NominatimReverseGeocodingDto {
        calls++
        lastCoordinates = coordinates
        exceptionToThrow?.let { throw it }
        return dtoToReturn
            ?: throw IllegalStateException("Missing NominatimReverseGeocodingDto")
    }
}
