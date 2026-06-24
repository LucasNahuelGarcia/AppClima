package shared.domain.model

data class MoonPhaseData(
    val phase: MoonPhase,
    val illuminationPercent: Int,
) {
    companion object {
        val Unknown = MoonPhaseData(
            phase = MoonPhase.Unknown,
            illuminationPercent = 0
        )
    }
}
