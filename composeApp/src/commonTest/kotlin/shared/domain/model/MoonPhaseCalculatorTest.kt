package shared.domain.model

import kotlin.math.roundToLong
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.datetime.Instant

class MoonPhaseCalculatorTest {

    @Test
    fun should_return_new_moon_at_reference_instant() {
        val result = calculateMoonPhase(REFERENCE_NEW_MOON)

        assertEquals(MoonPhase.NewMoon, result.phase)
        assertEquals(0, result.illuminationPercent)
    }

    @Test
    fun should_calculate_primary_moon_phases() {
        assertMoonPhase(
            ageDays = SYNODIC_MONTH_DAYS / 4,
            expectedPhase = MoonPhase.FirstQuarter,
            expectedIlluminationPercent = 50
        )
        assertMoonPhase(
            ageDays = SYNODIC_MONTH_DAYS / 2,
            expectedPhase = MoonPhase.FullMoon,
            expectedIlluminationPercent = 100
        )
        assertMoonPhase(
            ageDays = SYNODIC_MONTH_DAYS * 3 / 4,
            expectedPhase = MoonPhase.LastQuarter,
            expectedIlluminationPercent = 50
        )
    }

    @Test
    fun should_calculate_intermediate_moon_phases() {
        assertMoonPhase(ageDays = 3.0, expectedPhase = MoonPhase.WaxingCrescent)
        assertMoonPhase(ageDays = 10.0, expectedPhase = MoonPhase.WaxingGibbous)
        assertMoonPhase(ageDays = 18.0, expectedPhase = MoonPhase.WaningGibbous)
        assertMoonPhase(ageDays = 25.0, expectedPhase = MoonPhase.WaningCrescent)
    }

    @Test
    fun should_normalize_dates_before_reference_new_moon() {
        val result = calculateMoonPhase(instantAtLunarAge(-SYNODIC_MONTH_DAYS))

        assertEquals(MoonPhase.NewMoon, result.phase)
        assertEquals(0, result.illuminationPercent)
    }

    private fun assertMoonPhase(
        ageDays: Double,
        expectedPhase: MoonPhase,
        expectedIlluminationPercent: Int? = null
    ) {
        val result = calculateMoonPhase(instantAtLunarAge(ageDays))

        assertEquals(expectedPhase, result.phase)
        expectedIlluminationPercent?.let { expected ->
            assertEquals(expected, result.illuminationPercent)
        }
    }

    private fun instantAtLunarAge(ageDays: Double): Instant {
        val offsetMillis = (ageDays * MILLIS_PER_DAY).roundToLong()
        return Instant.fromEpochMilliseconds(REFERENCE_NEW_MOON.toEpochMilliseconds() + offsetMillis)
    }

    private companion object {
        const val SYNODIC_MONTH_DAYS = 29.53058867
        const val MILLIS_PER_DAY = 86_400_000.0
        val REFERENCE_NEW_MOON = Instant.parse("2000-01-06T18:14:00Z")
    }
}
