package shared.presentation.dashboard

import dydsproject.composeapp.generated.resources.Res
import dydsproject.composeapp.generated.resources.dia_despejado
import dydsproject.composeapp.generated.resources.dia_lluvioso
import dydsproject.composeapp.generated.resources.dia_nublado
import dydsproject.composeapp.generated.resources.noche_despejado
import dydsproject.composeapp.generated.resources.noche_lluvioso
import dydsproject.composeapp.generated.resources.noche_nublado
import org.jetbrains.compose.resources.DrawableResource
import shared.domain.model.DayNight

internal fun dashboardBackgroundResource(
    presentation: DashboardPresentation?
): DrawableResource {
    if (presentation == null) {
        return Res.drawable.dia_despejado
    }

    return when (presentation.weather.condition) {
        shared.domain.model.WeatherCondition.Clear -> if (presentation.weather.dayNight == DayNight.Night) {
            Res.drawable.noche_despejado
        } else {
            Res.drawable.dia_despejado
        }
        shared.domain.model.WeatherCondition.Cloudy -> if (presentation.weather.dayNight == DayNight.Night) {
            Res.drawable.noche_nublado
        } else {
            Res.drawable.dia_nublado
        }
        shared.domain.model.WeatherCondition.Rainy -> if (presentation.weather.dayNight == DayNight.Night) {
            Res.drawable.noche_lluvioso
        } else {
            Res.drawable.dia_lluvioso
        }
    }
}