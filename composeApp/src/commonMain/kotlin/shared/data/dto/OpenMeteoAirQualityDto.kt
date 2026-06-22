package shared.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenMeteoAirQualityDto(
    val latitude: Double,
    val longitude: Double,
    @SerialName("current") val current: OpenMeteoAirQualityCurrentDto = OpenMeteoAirQualityCurrentDto()
)

@Serializable
data class OpenMeteoAirQualityCurrentDto(
    @SerialName("time") val time: String? = null,
    @SerialName("european_aqi") val europeanAqi: Double? = null,
    @SerialName("us_aqi") val usAqi: Double? = null,
    @SerialName("pm10") val pm10: Double? = null,
    @SerialName("pm2_5") val pm2_5: Double? = null,
    @SerialName("ozone") val ozone: Double? = null
)