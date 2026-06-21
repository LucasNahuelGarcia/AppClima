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

class NominatimReverseGeocodingRemoteDataSourceTest {

    @Test
    fun should_request_nominatim_with_expected_params() = runTest {
        val coordinates = GeoCoordinates(latitude = -38.7167, longitude = -62.2833)
        val client = HttpClient(MockEngine { request ->
            assertEquals("/reverse", request.url.encodedPath)
            assertEquals("jsonv2", request.url.parameters["format"])
            assertEquals(coordinates.latitude.toString(), request.url.parameters["lat"])
            assertEquals(coordinates.longitude.toString(), request.url.parameters["lon"])
            assertEquals("1", request.url.parameters["addressdetails"])
            assertEquals("10", request.url.parameters["zoom"])
            assertEquals("address", request.url.parameters["layer"])
            assertEquals("es", request.url.parameters["accept-language"])

            respond(
                content = """
                {
                  "display_name": "Bahia Blanca, Buenos Aires, Argentina",
                  "address": {
                    "city": "Bahia Blanca",
                    "state": "Buenos Aires",
                    "country": "Argentina",
                    "country_code": "ar"
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

        val dataSource = NominatimReverseGeocodingRemoteDataSource(client)
        val result = dataSource.getLocation(coordinates)

        assertEquals("Bahia Blanca, Buenos Aires, Argentina", result.displayName)
        assertEquals("Bahia Blanca", result.address?.city)
        assertEquals("Buenos Aires", result.address?.state)
        assertEquals("Argentina", result.address?.country)
        assertEquals("ar", result.address?.countryCode)
    }
}
