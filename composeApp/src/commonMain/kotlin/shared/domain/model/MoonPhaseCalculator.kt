package shared.domain.model

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

private const val SYNODIC_MONTH_DAYS = 29.53058867
private val REFERENCE_NEW_MOON = Instant.parse("2000-01-06T18:14:00Z")

fun calculateMoonPhase(now: Instant = Clock.System.now()): MoonPhaseData {
    val age = moonAgeDays(now)
    val phase = moonPhaseFromAge(age)
    val illuminationPercent = moonIlluminationPercent(age)

    return MoonPhaseData(
        phase = phase,
        illuminationPercent = illuminationPercent
    )
}

private fun moonAgeDays(now: Instant): Double {
    val elapsedDays = (now.epochSeconds - REFERENCE_NEW_MOON.epochSeconds).toDouble() / 86_400.0
    val normalized = elapsedDays % SYNODIC_MONTH_DAYS
    return if (normalized < 0) normalized + SYNODIC_MONTH_DAYS else normalized
}

private fun moonPhaseFromAge(age: Double): MoonPhase {
    return when {
        age < 1.84566 -> MoonPhase.NewMoon
        age < 5.53699 -> MoonPhase.WaxingCrescent
        age < 9.22831 -> MoonPhase.FirstQuarter
        age < 12.91963 -> MoonPhase.WaxingGibbous
        age < 16.61096 -> MoonPhase.FullMoon
        age < 20.30228 -> MoonPhase.WaningGibbous
        age < 23.99361 -> MoonPhase.LastQuarter
        age < 27.68493 -> MoonPhase.WaningCrescent
        else -> MoonPhase.NewMoon
    }
}

private fun moonIlluminationPercent(age: Double): Int {
    val illumination = ((1 - cos(2 * PI * age / SYNODIC_MONTH_DAYS)) / 2 * 100).roundToInt()
    return illumination.coerceIn(0, 100)
}