package shared.data.datasource.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import shared.domain.model.GeoCoordinates

class OpenMeteoAirQualityRemoteDataSourceTest {

    @Test
    fun should_request_open_meteo_air_quality_with_expected_params() = runTest {
        val coordinates = GeoCoordinates(latitude = -38.7167, longitude = -62.2833)
        val client = HttpClient(MockEngine { request ->
            assertEquals("/v1/air-quality", request.url.encodedPath)
            assertEquals(coordinates.latitude.toString(), request.url.parameters["latitude"])
            assertEquals(coordinates.longitude.toString(), request.url.parameters["longitude"])
            assertEquals("european_aqi,us_aqi,pm10,pm2_5,ozone", request.url.parameters["current"])
            assertEquals("1", request.url.parameters["forecast_days"])
            assertEquals("UTC", request.url.parameters["timezone"])

            respond(
                content = """
                {
                    "latitude": -38.7167,
                    "longitude": -62.2833,
                    "current": {
                        "time": "2026-06-21T12:00:00Z",
                        "european_aqi": 42.0,
                        "us_aqi": 58.0,
                        "pm10": 12.5,
                        "pm2_5": 8.4,
                        "ozone": 31.2
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

        val dataSource = OpenMeteoAirQualityRemoteDataSource(client)
        val result = dataSource.getAirQuality(coordinates)

        assertEquals(-38.7167, result.latitude)
        assertEquals(-62.2833, result.longitude)
        assertEquals("2026-06-21T12:00:00Z", result.current.time)
        assertEquals(42.0, result.current.europeanAqi)
        assertEquals(58.0, result.current.usAqi)
        assertEquals(12.5, result.current.pm10)
        assertEquals(8.4, result.current.pm2_5)
        assertEquals(31.2, result.current.ozone)
    }
}