package shared.domain.usecase

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import shared.domain.model.AirQualityData
import shared.domain.model.AirQualityLevel
import shared.domain.model.DashboardData
import shared.domain.model.DomainError
import shared.domain.model.GeoCoordinates
import shared.domain.model.DayNight
import shared.domain.model.calculateMoonPhase
import shared.domain.model.WeatherCondition
import shared.domain.model.WeatherData
import shared.fake.FakeAirQualityRepository
import shared.fake.FakeWeatherRepository

class GetDashboardDataUseCaseImplTest {

    @Test
    fun should_return_success_when_both_ok() = runTest {
        val coordinates = GeoCoordinates(latitude = -38.7167, longitude = -62.2833)
        val weather = buildWeather(coordinates)
        val airQuality = buildAirQuality(coordinates)
        val now = Instant.parse("2026-06-02T12:00:00Z")

        val weatherRepository = FakeWeatherRepository(Result.success(weather))
        val airQualityRepository = FakeAirQualityRepository(Result.success(airQuality))
        val useCase = GetDashboardDataUseCaseImpl(weatherRepository, airQualityRepository) { now }

        val result = useCase(coordinates)

        assertTrue(result.isSuccess)
        assertEquals(
            DashboardData(weather = weather, moonPhase = calculateMoonPhase(now), airQuality = airQuality),
            result.getOrThrow()
        )
        assertEquals(1, weatherRepository.calls)
        assertEquals(1, airQualityRepository.calls)
        assertEquals(coordinates, weatherRepository.lastCoordinates)
        assertEquals(coordinates, airQualityRepository.lastCoordinates)
    }

    @Test
    fun should_return_failure_when_weather_fails() = runTest {
        val coordinates = GeoCoordinates(latitude = 10.0, longitude = 20.0)
        val weatherError = IllegalStateException("weather failed")
        val now = Instant.parse("2026-06-02T12:00:00Z")

        val weatherRepository = FakeWeatherRepository(Result.failure(weatherError))
        val airQualityRepository = FakeAirQualityRepository(Result.success(buildAirQuality(coordinates)))
        val useCase = GetDashboardDataUseCaseImpl(weatherRepository, airQualityRepository) { now }

        val result = useCase(coordinates)

        assertTrue(result.isFailure)
        val error = assertIs<DomainError.DashboardFetchFailed>(result.exceptionOrNull())
        assertEquals(weatherError, error.cause)
        assertEquals(1, weatherRepository.calls)
        assertEquals(1, airQualityRepository.calls)
    }

    @Test
    fun should_keep_dashboard_when_air_quality_fails() = runTest {
        val coordinates = GeoCoordinates(latitude = 10.0, longitude = 20.0)
        val now = Instant.parse("2026-06-02T12:00:00Z")

        val weather = buildWeather(coordinates)
        val weatherRepository = FakeWeatherRepository(Result.success(weather))
        val airQualityRepository = FakeAirQualityRepository(Result.failure(IllegalStateException("air quality failed")))
        val useCase = GetDashboardDataUseCaseImpl(weatherRepository, airQualityRepository) { now }

        val result = useCase(coordinates)

        assertTrue(result.isSuccess)
        assertEquals(
            DashboardData(weather = weather, moonPhase = calculateMoonPhase(now), airQuality = null),
            result.getOrThrow()
        )
        assertEquals(1, weatherRepository.calls)
        assertEquals(1, airQualityRepository.calls)
    }

    private fun buildWeather(coordinates: GeoCoordinates): WeatherData {
        return WeatherData(
            coordinates = coordinates,
            temperatureCelsius = 18.4,
            windSpeedKmh = 12.5,
            condition = WeatherCondition.Cloudy,
            dayNight = DayNight.Day
        )
    }

    private fun buildAirQuality(coordinates: GeoCoordinates): AirQualityData {
        return AirQualityData(
            coordinates = coordinates,
            europeanAqi = 42,
            usAqi = 58,
            pm10 = 12.5,
            pm2_5 = 8.4,
            ozone = 31.2,
            level = AirQualityLevel.Moderate
        )
    }
}
