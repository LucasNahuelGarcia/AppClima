package shared.domain.model

data class AstronomyData(
    val title: String,
    val date: String,
    val explanation: String,
    val mediaType: String,
    val url: String,
    val hdUrl: String?,
    val moonPhase: MoonPhaseData = MoonPhaseData.Unknown
)
