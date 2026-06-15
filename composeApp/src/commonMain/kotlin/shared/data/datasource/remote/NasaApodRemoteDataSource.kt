package shared.data.datasource.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import shared.data.dto.NasaApodDto

class NasaApodRemoteDataSource(
    private val client: HttpClient,
    private val apiKeyProvider: () -> String
) : AstronomyRemoteDataSource {

    override suspend fun getAstronomyData(): NasaApodDto {
        return client.get("https://api.nasa.gov/planetary/apod") {
            parameter("api_key", apiKeyProvider())
        }.body()
    }
}
