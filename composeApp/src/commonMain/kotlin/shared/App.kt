package shared

import androidx.compose.runtime.Composable
import shared.domain.model.GeoCoordinates
import shared.presentation.screen.DashboardScreen
import shared.presentation.viewmodel.DashboardViewModel

private val defaultCoordinates = GeoCoordinates(
    latitude = -38.7183,
    longitude = -62.2663
)

@Composable
fun App(dashboardViewModel: DashboardViewModel, coordinates: GeoCoordinates = defaultCoordinates) {
    DashboardScreen(
        viewModel = dashboardViewModel,
        coordinates = coordinates
    )
}
