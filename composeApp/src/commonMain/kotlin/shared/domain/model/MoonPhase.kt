package shared.domain.model

enum class MoonPhase(
    val displayName: String
) {
    Unknown("Sin datos"),
    NewMoon("Luna nueva"),
    FullMoon("Luna llena"),
    FirstQuarter("Cuarto creciente"),
    LastQuarter("Cuarto menguante"),
    WaxingCrescent("Luna creciente"),
    WaxingGibbous("Gibosa creciente"),
    WaningCrescent("Luna menguante"),
    WaningGibbous("Gibosa menguante");

    companion object {
        fun fromBackendValue(value: String): MoonPhase {
            return when (value.normalizedKey()) {
                "sin_datos", "unknown", "desconocida" -> Unknown
                "new_moon", "new", "luna_nueva" -> NewMoon
                "full_moon", "full", "luna_llena", "llena" -> FullMoon
                "first_quarter", "cuarto_creciente" -> FirstQuarter
                "last_quarter", "cuarto_menguante" -> LastQuarter
                "waxing_crescent", "creciente" -> WaxingCrescent
                "waxing_gibbous" -> WaxingGibbous
                "waning_crescent", "menguante" -> WaningCrescent
                "waning_gibbous" -> WaningGibbous
                else -> Unknown
            }
        }
    }
}

private fun String.normalizedKey(): String {
    return lowercase().trim().replace(' ', '_')
}