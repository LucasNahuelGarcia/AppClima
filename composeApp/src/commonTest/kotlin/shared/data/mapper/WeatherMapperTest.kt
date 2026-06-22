package shared.data.mapper

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.datetime.Instant
import shared.data.dto.OpenMeteoCurrentDto
import shared.data.dto.OpenMeteoHourlyDto
import shared.data.dto.OpenMeteoWeatherDto
import shared.domain.model.GeoCoordinates
import shared.domain.model.HourlyForecast
import shared.domain.model.DayNight
import shared.domain.model.WeatherCondition

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
            ),
            hourly = OpenMeteoHourlyDto(
                time = listOf(
                    "2026-06-21T11:00:00Z",
                    "2026-06-21T12:00:00Z",
                    "2026-06-21T13:00:00Z"
                ),
                temperatureCelsius = listOf(17.2, 18.0, 19.1),
                weatherCode = listOf(1, 2, 3)
            )
        )

        val result = dto.toDomainModel { Instant.parse("2026-06-21T12:34:56Z") }

        assertEquals(GeoCoordinates(-38.7167, -62.2833), result.coordinates)
        assertEquals(18.4, result.temperatureCelsius)
        assertEquals(12.5, result.windSpeedKmh)
        assertEquals(WeatherCondition.Cloudy, result.condition)
        assertEquals(DayNight.Day, result.dayNight)
        assertEquals(
            listOf(
                HourlyForecast(time = "12:00", temperatureCelsius = 18, condition = WeatherCondition.Cloudy),
                HourlyForecast(time = "13:00", temperatureCelsius = 19, condition = WeatherCondition.Cloudy)
            ),
            result.hourlyForecast
        )
    }
}
