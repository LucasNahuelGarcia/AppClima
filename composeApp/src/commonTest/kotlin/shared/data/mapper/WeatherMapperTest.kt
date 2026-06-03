package shared.data.mapper

import kotlin.test.Test
import kotlin.test.assertEquals
import shared.data.dto.OpenMeteoCurrentDto
import shared.data.dto.OpenMeteoWeatherDto
import shared.domain.model.GeoCoordinates

class WeatherMapperTest {

    @Test
    fun should_map_open_meteo_to_domain() {
        val dto = OpenMeteoWeatherDto(
            latitude = -38.7167,
            longitude = -62.2833,
            current = OpenMeteoCurrentDto(
                time = "2026-06-02T12:00:00Z",
                temperatureCelsius = 18.4,
                windSpeedKmh = 12.5,
                weatherCode = 3
            )
        )

        val result = dto.toDomainModel()

        assertEquals(GeoCoordinates(-38.7167, -62.2833), result.coordinates)
        assertEquals(18.4, result.temperatureCelsius)
        assertEquals(12.5, result.windSpeedKmh)
        assertEquals(3, result.weatherCode)
        assertEquals("2026-06-02T12:00:00Z", result.timeIso)
    }
}
