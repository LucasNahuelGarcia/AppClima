package shared.data.repository

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import shared.data.dto.OpenMeteoAirQualityCurrentDto
import shared.data.dto.OpenMeteoAirQualityDto
import shared.domain.model.AirQualityLevel
import shared.domain.model.GeoCoordinates
import shared.fake.FakeAirQualityRemoteDataSource

class AirQualityRepositoryImplSuccessTest {

    @Test
    fun should_return_air_quality_when_remote_ok() = runTest {
        val coordinates = GeoCoordinates(latitude = -38.7167, longitude = -62.2833)
        val dto = OpenMeteoAirQualityDto(
            latitude = coordinates.latitude,
            longitude = coordinates.longitude,
            current = OpenMeteoAirQualityCurrentDto(
                time = "2026-06-21T12:00:00Z",
                europeanAqi = 42.0,
                usAqi = 58.0,
                pm10 = 12.5,
                pm2_5 = 8.4,
                ozone = 31.2
            )
        )
        val remote = FakeAirQualityRemoteDataSource(dtoToReturn = dto)
        val repository = AirQualityRepositoryImpl(remote)

        val result = repository.getAirQuality(coordinates)

        assertTrue(result.isSuccess)
        assertEquals(coordinates, result.getOrThrow().coordinates)
        assertEquals(42, result.getOrThrow().europeanAqi)
        assertEquals(58, result.getOrThrow().usAqi)
        assertEquals(12.5, result.getOrThrow().pm10)
        assertEquals(8.4, result.getOrThrow().pm2_5)
        assertEquals(31.2, result.getOrThrow().ozone)
        assertEquals(AirQualityLevel.Moderate, result.getOrThrow().level)
        assertEquals(1, remote.calls)
        assertEquals(coordinates, remote.lastCoordinates)
    }
}