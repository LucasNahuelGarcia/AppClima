package shared.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import shared.domain.model.GeoCoordinates
import shared.presentation.dashboard.DashboardRoute
import shared.presentation.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    coordinates: GeoCoordinates,
    modifier: Modifier = Modifier
) {
    val state = viewModel.uiState.collectAsState().value
    val locationState = viewModel.locationState.collectAsState().value

    DashboardRoute(
        state = state,
        locationState = locationState,
        coordinates = coordinates,
        modifier = modifier,
        onLoadDashboard = viewModel::loadDashboard,
        onRetry = viewModel::retry
    )
}
