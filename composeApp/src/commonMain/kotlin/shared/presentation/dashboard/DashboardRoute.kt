package shared.presentation.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import shared.domain.model.GeoCoordinates
import shared.domain.model.LocationData
import shared.presentation.state.UiState

@Composable
internal fun DashboardRoute(
    state: UiState<shared.domain.model.DashboardData>,
    locationState: UiState<LocationData>,
    coordinates: GeoCoordinates,
    refreshKey: Int,
    currentPage: Int,
    pageCount: Int,
    isCurrentPage: Boolean,
    modifier: Modifier = Modifier,
    onLoadDashboard: (GeoCoordinates) -> Unit,
    onRefresh: () -> Unit,
    onOpenLocationsWindow: () -> Unit
) {
    LaunchedEffect(coordinates, refreshKey, isCurrentPage) {
        if (isCurrentPage) {
            onLoadDashboard(coordinates)
        }
    }

    val dashboardUiState = state.toDashboardUiState(locationState)

    DashboardTemplate(
        uiState = dashboardUiState,
        currentPage = currentPage,
        pageCount = pageCount,
        modifier = modifier,
        onRefresh = onRefresh,
        onOpenLocationsWindow = onOpenLocationsWindow
    )
}
