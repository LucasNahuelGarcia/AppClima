package shared.data.mapper

import kotlin.test.Test
import kotlin.test.assertEquals
import shared.data.dto.OpenMeteoAirQualityCurrentDto
import shared.data.dto.OpenMeteoAirQualityDto
import shared.domain.model.AirQualityLevel

class AirQualityMapperTest {

    @Test
    fun should_map_air_quality_dto_to_domain_model() {
        val dto = OpenMeteoAirQualityDto(
            latitude = -38.7167,
            longitude = -62.2833,
            current = OpenMeteoAirQualityCurrentDto(
                time = "2026-06-21T12:00:00Z",
                europeanAqi = 42.0,
                usAqi = 58.0,
                pm10 = 12.5,
                pm2_5 = 8.4,
                ozone = 31.2
            )
        )

        val result = dto.toDomainModel()

        assertEquals(-38.7167, result.coordinates.latitude)
        assertEquals(-62.2833, result.coordinates.longitude)
        assertEquals(42, result.europeanAqi)
        assertEquals(58, result.usAqi)
        assertEquals(12.5, result.pm10)
        assertEquals(8.4, result.pm2_5)
        assertEquals(31.2, result.ozone)
        assertEquals(AirQualityLevel.Moderate, result.level)
    }
}