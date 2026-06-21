package shared.domain.model

data class HourlyForecast(
    val time: String,
    val temperatureCelsius: Int,
    val weatherCode: Int
)

data class MoonPhaseData(
    val phaseName: String,
    val illuminationPercent: Int,
    val iconUrl: String
) {
    companion object {
        val Unknown = MoonPhaseData(
            phaseName = "Sin datos",
            illuminationPercent = 0,
            iconUrl = ""
        )
    }
}
