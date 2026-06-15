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

class NasaApodRemoteDataSourceTest {

    @Test
    fun should_request_nasa_apod_with_api_key() = kotlinx.coroutines.test.runTest {
        val client = HttpClient(MockEngine { request ->
            assertEquals("/planetary/apod", request.url.encodedPath)
            assertEquals("test-key", request.url.parameters["api_key"])

            respond(
                content = """
                {
                                    "title": "Milky Way",
                                    "date": "2026-06-02",
                                    "explanation": "Night sky view",
                                    "media_type": "image",
                                    "url": "https://example.com/apod.jpg",
                                    "hdurl": "https://example.com/apod_hd.jpg"
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

        val dataSource = NasaApodRemoteDataSource(
            client = client,
            apiKeyProvider = { "test-key" }
        )
        val result = dataSource.getAstronomyData()

        assertEquals("Milky Way", result.title)
        assertEquals("2026-06-02", result.date)
        assertEquals("Night sky view", result.explanation)
        assertEquals("image", result.mediaType)
        assertEquals("https://example.com/apod.jpg", result.url)
        assertEquals("https://example.com/apod_hd.jpg", result.hdUrl)
    }
}
