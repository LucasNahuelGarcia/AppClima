package shared.data.mapper

import kotlin.math.roundToInt
import shared.data.dto.OpenMeteoAirQualityDto
import shared.domain.model.AirQualityData
import shared.domain.model.AirQualityLevel
import shared.domain.model.GeoCoordinates

fun OpenMeteoAirQualityDto.toDomainModel(): AirQualityData {
    val europeanAqi = current.europeanAqi?.roundToInt()

    return AirQualityData(
        coordinates = GeoCoordinates(latitude = latitude, longitude = longitude),
        europeanAqi = europeanAqi,
        usAqi = current.usAqi?.roundToInt(),
        pm10 = current.pm10,
        pm2_5 = current.pm2_5,
        ozone = current.ozone,
        level = AirQualityLevel.fromEuropeanAqi(europeanAqi)
    )
}