package shared.data.repository

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import shared.data.dto.OpenMeteoCurrentDto
import shared.data.dto.OpenMeteoHourlyDto
import shared.data.dto.OpenMeteoWeatherDto
import shared.domain.model.GeoCoordinates
import shared.domain.model.HourlyForecast
import shared.domain.model.DayNight
import shared.domain.model.WeatherCondition
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
            ),
            hourly = OpenMeteoHourlyDto(
                time = listOf(
                    "2026-06-21T12:00:00Z",
                    "2026-06-21T13:00:00Z"
                ),
                temperatureCelsius = listOf(18.0, 19.1),
                weatherCode = listOf(2, 3)
            )
        )
        val remote = FakeWeatherRemoteDataSource(dtoToReturn = dto)
        val repository = WeatherRepositoryImpl(remote) { Instant.parse("2026-06-21T12:34:56Z") }

        val result = repository.getCurrentWeather(coordinates)

        assertTrue(result.isSuccess)
        assertEquals(
            WeatherData(
                coordinates = coordinates,
                temperatureCelsius = 18.4,
                windSpeedKmh = 12.5,
                condition = WeatherCondition.Cloudy,
                dayNight = DayNight.Day,
                hourlyForecast = listOf(
                    HourlyForecast(time = "12:00", temperatureCelsius = 18, condition = WeatherCondition.Cloudy),
                    HourlyForecast(time = "13:00", temperatureCelsius = 19, condition = WeatherCondition.Cloudy)
                )
            ),
            result.getOrThrow()
        )
        assertEquals(1, remote.calls)
        assertEquals(coordinates, remote.lastCoordinates)
    }
}
