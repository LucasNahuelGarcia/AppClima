package shared.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createWeatherHttpClient(): HttpClient = HttpClient {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 15000
        connectTimeoutMillis = 10000
    }
    install(DefaultRequest) {
        url {
            protocol = URLProtocol.HTTPS
            host = "api.open-meteo.com"
        }
    }
    install(Logging) {
        level = LogLevel.INFO
    }
}

fun createNasaHttpClient(): HttpClient = HttpClient {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 15000
        connectTimeoutMillis = 10000
    }
    install(DefaultRequest) {
        url {
            protocol = URLProtocol.HTTPS
            host = "api.nasa.gov"
        }
    }
    install(Logging) {
        level = LogLevel.INFO
    }
}

