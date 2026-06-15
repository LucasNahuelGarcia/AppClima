package shared.data.datasource.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import shared.domain.model.GeoCoordinates

class OpenMeteoWeatherRemoteDataSourceTest {

    @Test
    fun should_request_open_meteo_with_expected_params() = kotlinx.coroutines.test.runTest {
        val coordinates = GeoCoordinates(latitude = -38.7167, longitude = -62.2833)
        val client = HttpClient(MockEngine { request ->
            assertEquals("/v1/forecast", request.url.encodedPath)
            assertEquals(coordinates.latitude.toString(), request.url.parameters["latitude"])
            assertEquals(coordinates.longitude.toString(), request.url.parameters["longitude"])
            assertEquals("temperature_2m,weather_code,wind_speed_10m", request.url.parameters["current"])
            assertEquals("UTC", request.url.parameters["timezone"])

            respond(
                content = """
                {
                                    "latitude": -38.7167,
                                    "longitude": -62.2833,
                                    "current": {
                                        "time": "2026-06-02T12:00:00Z",
                                        "temperature_2m": 18.4,
                                        "wind_speed_10m": 12.5,
                                        "weather_code": 3
                  }
                }
                """.trimIndent(),
                status = HttpStatusCode.OK,
                headers = headersOf("Content-Type", "application/json")
            )
        }) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val dataSource = OpenMeteoWeatherRemoteDataSource(client)
        val result = dataSource.getCurrentWeather(coordinates)

        assertEquals(-38.7167, result.latitude)
        assertEquals(-62.2833, result.longitude)
        assertEquals("2026-06-02T12:00:00Z", result.current.time)
        assertEquals(18.4, result.current.temperatureCelsius)
        assertEquals(12.5, result.current.windSpeedKmh)
        assertEquals(3, result.current.weatherCode)
    }
}
