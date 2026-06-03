package shared.data.datasource.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import shared.data.dto.OpenMeteoWeatherDto
import shared.domain.model.GeoCoordinates

class OpenMeteoWeatherRemoteDataSource(
    private val client: HttpClient
) : WeatherRemoteDataSource {

    override suspend fun getCurrentWeather(coordinates: GeoCoordinates): OpenMeteoWeatherDto {
        return client.get("/v1/forecast") {
            parameter("latitude", coordinates.latitude)
            parameter("longitude", coordinates.longitude)
            parameter("current", "temperature_2m,weather_code,wind_speed_10m")
            parameter("timezone", "UTC")
        }.body()
    }
}
