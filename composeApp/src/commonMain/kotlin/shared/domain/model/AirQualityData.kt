package shared.domain.model

data class AirQualityData(
    val coordinates: GeoCoordinates,
    val europeanAqi: Int? = null,
    val usAqi: Int? = null,
    val pm10: Double? = null,
    val pm2_5: Double? = null,
    val ozone: Double? = null,
    val level: AirQualityLevel = AirQualityLevel.Unknown
) {
    companion object {
        val Unknown = AirQualityData(
            coordinates = GeoCoordinates(latitude = 0.0, longitude = 0.0)
        )
    }
}

enum class AirQualityLevel(
    val displayName: String,
    val description: String
) {
    Good("Buena", "Aire saludable"),
    Fair("Aceptable", "Sin riesgo relevante para la mayoría"),
    Moderate("Moderada", "Sensibles deben prestar atención"),
    Poor("Mala", "Reducir actividad intensa al aire libre"),
    VeryPoor("Muy mala", "Evitar exposición prolongada"),
    Extreme("Extrema", "Permanecer en interiores si es posible"),
    Unknown("Sin datos", "No se pudo evaluar la calidad del aire");

    companion object {
        fun fromEuropeanAqi(index: Int?): AirQualityLevel {
            return when {
                index == null -> Unknown
                index <= 20 -> Good
                index <= 40 -> Fair
                index <= 60 -> Moderate
                index <= 80 -> Poor
                index <= 100 -> VeryPoor
                else -> Extreme
            }
        }
    }
}