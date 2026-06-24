package shared.domain.model

data class HourlyForecast(
    val time: String,
    val temperatureCelsius: Int,
    val condition: WeatherCondition
)
