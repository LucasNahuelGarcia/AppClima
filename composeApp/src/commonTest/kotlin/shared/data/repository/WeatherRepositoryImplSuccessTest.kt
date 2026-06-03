package shared.data.repository

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import shared.data.dto.OpenMeteoCurrentDto
import shared.data.dto.OpenMeteoWeatherDto
import shared.domain.model.GeoCoordinates
import shared.domain.model.WeatherData
import shared.fake.FakeWeatherRemoteDataSource

class WeatherRepositoryImplSuccessTest {

    @Test
    fun should_return_weather_when_remote_ok() = runTest {
        val coordinates = GeoCoordinates(latitude = -38.7167, longitude = -62.2833)
        val dto = OpenMeteoWeatherDto(
            latitude = coordinates.latitude,
            longitude = coordinates.longitude,
            current = OpenMeteoCurrentDto(
                time = "2026-06-02T12:00:00Z",
                temperatureCelsius = 18.4,
                windSpeedKmh = 12.5,
                weatherCode = 3
            )
        )
        val remote = FakeWeatherRemoteDataSource(dtoToReturn = dto)
        val repository = WeatherRepositoryImpl(remote)

        val result = repository.getCurrentWeather(coordinates)

        assertTrue(result.isSuccess)
        assertEquals(
            WeatherData(
                coordinates = coordinates,
                temperatureCelsius = 18.4,
                windSpeedKmh = 12.5,
                weatherCode = 3,
                timeIso = "2026-06-02T12:00:00Z"
            ),
            result.getOrThrow()
        )
        assertEquals(1, remote.calls)
        assertEquals(coordinates, remote.lastCoordinates)
    }
}
