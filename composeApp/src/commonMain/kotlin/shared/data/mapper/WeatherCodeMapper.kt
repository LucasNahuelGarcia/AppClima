package shared.data.mapper

import shared.domain.model.DayNight
import shared.domain.model.WeatherCondition

internal fun Int.toWeatherCondition(): WeatherCondition {
    return when (this) {
        0 -> WeatherCondition.Clear
        1, 2, 3, 45, 48 -> WeatherCondition.Cloudy
        in 51..67, in 71..77, in 80..82, in 85..86, in 95..99 -> WeatherCondition.Rainy
        else -> WeatherCondition.Cloudy
    }
}

internal fun Int.toDayNight(): DayNight {
    return if (this == 0) DayNight.Night else DayNight.Day
}