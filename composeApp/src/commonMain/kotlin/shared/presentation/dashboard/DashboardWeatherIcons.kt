package shared.presentation.dashboard

import dydsproject.composeapp.generated.resources.Res
import dydsproject.composeapp.generated.resources.Icono_Despejado_Noche
import dydsproject.composeapp.generated.resources.Icono_despejado_Dia
import dydsproject.composeapp.generated.resources.Icono_lluvioso
import dydsproject.composeapp.generated.resources.Icono_nublado
import org.jetbrains.compose.resources.DrawableResource
import shared.domain.model.DayNight
import shared.domain.model.WeatherCondition

internal fun dashboardHeroWeatherIconResource(
    condition: WeatherCondition,
    dayNight: DayNight
): DrawableResource {
    return when (condition) {
        WeatherCondition.Clear -> if (dayNight == DayNight.Night) {
            Res.drawable.Icono_Despejado_Noche
        } else {
            Res.drawable.Icono_despejado_Dia
        }
        WeatherCondition.Cloudy -> Res.drawable.Icono_nublado
        WeatherCondition.Rainy -> Res.drawable.Icono_lluvioso
    }
}

internal fun dashboardHourlyWeatherIconResource(condition: WeatherCondition): DrawableResource {
    return when (condition) {
        WeatherCondition.Clear -> Res.drawable.Icono_despejado_Dia
        WeatherCondition.Cloudy -> Res.drawable.Icono_nublado
        WeatherCondition.Rainy -> Res.drawable.Icono_lluvioso
    }
}