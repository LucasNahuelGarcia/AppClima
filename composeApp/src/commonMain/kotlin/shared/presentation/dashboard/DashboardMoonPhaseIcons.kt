package shared.presentation.dashboard

import dydsproject.composeapp.generated.resources.Res
import dydsproject.composeapp.generated.resources.first_quarter_moon_09ea6a
import dydsproject.composeapp.generated.resources.full_moon_40af26
import dydsproject.composeapp.generated.resources.last_quarter_moon_4090c3
import dydsproject.composeapp.generated.resources.waning_crescent_moon_e0a755
import dydsproject.composeapp.generated.resources.waning_gibbous_moon_3d46a0
import dydsproject.composeapp.generated.resources.waxing_crescent_moon_517747
import dydsproject.composeapp.generated.resources.waxing_gibbous_moon_0c681c
import org.jetbrains.compose.resources.DrawableResource
import shared.domain.model.MoonPhase

internal fun dashboardMoonPhaseIconResource(phase: MoonPhase): DrawableResource {
    return when (phase) {
        MoonPhase.Unknown,
        MoonPhase.FullMoon -> Res.drawable.full_moon_40af26
        MoonPhase.FirstQuarter -> Res.drawable.first_quarter_moon_09ea6a
        MoonPhase.LastQuarter -> Res.drawable.last_quarter_moon_4090c3
        MoonPhase.WaxingCrescent -> Res.drawable.waxing_crescent_moon_517747
        MoonPhase.WaxingGibbous -> Res.drawable.waxing_gibbous_moon_0c681c
        MoonPhase.WaningCrescent -> Res.drawable.waning_crescent_moon_e0a755
        MoonPhase.WaningGibbous -> Res.drawable.waning_gibbous_moon_3d46a0
    }
}