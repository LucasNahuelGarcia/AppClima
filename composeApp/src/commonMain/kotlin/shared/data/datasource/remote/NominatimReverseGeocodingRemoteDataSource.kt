package shared.data.datasource.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders
import shared.data.dto.NominatimReverseGeocodingDto
import shared.domain.model.GeoCoordinates

class NominatimReverseGeocodingRemoteDataSource(
    private val client: HttpClient
) : ReverseGeocodingRemoteDataSource {

    override suspend fun getLocation(coordinates: GeoCoordinates): NominatimReverseGeocodingDto {
        return client.get("https://nominatim.openstreetmap.org/reverse") {
            header(HttpHeaders.UserAgent, "DYDSProject/1.0")
            parameter("format", "jsonv2")
            parameter("lat", coordinates.latitude)
            parameter("lon", coordinates.longitude)
            parameter("addressdetails", 1)
            parameter("zoom", 10)
            parameter("layer", "address")
            parameter("accept-language", "es")
        }.body()
    }
}
