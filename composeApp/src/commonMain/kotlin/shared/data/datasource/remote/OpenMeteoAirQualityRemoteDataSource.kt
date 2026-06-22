package shared.data.datasource.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import shared.data.dto.OpenMeteoAirQualityDto
import shared.domain.model.GeoCoordinates

class OpenMeteoAirQualityRemoteDataSource(
    private val client: HttpClient
) : AirQualityRemoteDataSource {

    override suspend fun getAirQuality(coordinates: GeoCoordinates): OpenMeteoAirQualityDto {
        return client.get("https://air-quality-api.open-meteo.com/v1/air-quality") {
            parameter("latitude", coordinates.latitude)
            parameter("longitude", coordinates.longitude)
            parameter("current", "european_aqi,us_aqi,pm10,pm2_5,ozone")
            parameter("forecast_days", 1)
            parameter("timezone", "UTC")
        }.body()
    }
}