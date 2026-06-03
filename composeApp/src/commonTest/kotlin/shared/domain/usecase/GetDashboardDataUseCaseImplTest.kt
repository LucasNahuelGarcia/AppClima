package shared.domain.usecase

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import shared.domain.model.AstronomyData
import shared.domain.model.DashboardData
import shared.domain.model.DomainError
import shared.domain.model.GeoCoordinates
import shared.domain.model.WeatherData
import shared.fake.FakeAstronomyRepository
import shared.fake.FakeWeatherRepository

class GetDashboardDataUseCaseImplTest {

    @Test
    fun should_return_success_when_both_ok() = runTest {
        val coordinates = GeoCoordinates(latitude = -38.7167, longitude = -62.2833)
        val weather = buildWeather(coordinates)
        val astronomy = buildAstronomy()

        val weatherRepository = FakeWeatherRepository(Result.success(weather))
        val astronomyRepository = FakeAstronomyRepository(Result.success(astronomy))
        val useCase = GetDashboardDataUseCaseImpl(weatherRepository, astronomyRepository)

        val result = useCase(coordinates)

        assertTrue(result.isSuccess)
        assertEquals(DashboardData(weather = weather, astronomy = astronomy), result.getOrThrow())
        assertEquals(1, weatherRepository.calls)
        assertEquals(coordinates, weatherRepository.lastCoordinates)
        assertEquals(1, astronomyRepository.calls)
    }

    @Test
    fun should_return_failure_when_weather_fails() = runTest {
        val coordinates = GeoCoordinates(latitude = 10.0, longitude = 20.0)
        val weatherError = IllegalStateException("weather failed")

        val weatherRepository = FakeWeatherRepository(Result.failure(weatherError))
        val astronomyRepository = FakeAstronomyRepository(Result.success(buildAstronomy()))
        val useCase = GetDashboardDataUseCaseImpl(weatherRepository, astronomyRepository)

        val result = useCase(coordinates)

        assertTrue(result.isFailure)
        val error = assertIs<DomainError.DashboardFetchFailed>(result.exceptionOrNull())
        assertEquals(weatherError, error.cause)
        assertEquals(1, weatherRepository.calls)
        assertEquals(1, astronomyRepository.calls)
    }

    @Test
    fun should_return_failure_when_astronomy_fails() = runTest {
        val coordinates = GeoCoordinates(latitude = 10.0, longitude = 20.0)
        val astronomyError = IllegalStateException("astronomy failed")

        val weatherRepository = FakeWeatherRepository(Result.success(buildWeather(coordinates)))
        val astronomyRepository = FakeAstronomyRepository(Result.failure(astronomyError))
        val useCase = GetDashboardDataUseCaseImpl(weatherRepository, astronomyRepository)

        val result = useCase(coordinates)

        assertTrue(result.isFailure)
        val error = assertIs<DomainError.DashboardFetchFailed>(result.exceptionOrNull())
        assertEquals(astronomyError, error.cause)
        assertEquals(1, weatherRepository.calls)
        assertEquals(1, astronomyRepository.calls)
    }

    @Test
    fun should_prioritize_weather_error_when_both_fail() = runTest {
        val coordinates = GeoCoordinates(latitude = 10.0, longitude = 20.0)
        val weatherError = IllegalArgumentException("weather failed")
        val astronomyError = IllegalStateException("astronomy failed")

        val weatherRepository = FakeWeatherRepository(Result.failure(weatherError))
        val astronomyRepository = FakeAstronomyRepository(Result.failure(astronomyError))
        val useCase = GetDashboardDataUseCaseImpl(weatherRepository, astronomyRepository)

        val result = useCase(coordinates)

        assertTrue(result.isFailure)
        val error = assertIs<DomainError.DashboardFetchFailed>(result.exceptionOrNull())
        assertEquals(weatherError, error.cause)
        assertEquals(1, weatherRepository.calls)
        assertEquals(1, astronomyRepository.calls)
    }

    private fun buildWeather(coordinates: GeoCoordinates): WeatherData {
        return WeatherData(
            coordinates = coordinates,
            temperatureCelsius = 18.4,
            windSpeedKmh = 12.5,
            weatherCode = 3,
            timeIso = "2026-06-02T12:00:00Z"
        )
    }

    private fun buildAstronomy(): AstronomyData {
        return AstronomyData(
            title = "Milky Way",
            date = "2026-06-02",
            explanation = "Night sky view",
            mediaType = "image",
            url = "https://example.com/apod.jpg",
            hdUrl = "https://example.com/apod_hd.jpg"
        )
    }
}
