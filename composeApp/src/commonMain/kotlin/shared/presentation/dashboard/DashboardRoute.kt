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
    modifier: Modifier = Modifier,
    onLoadDashboard: (GeoCoordinates) -> Unit,
    onRetry: () -> Unit
) {
    LaunchedEffect(coordinates) {
        onLoadDashboard(coordinates)
    }

    val dashboardUiState = state.toDashboardUiState(locationState)
    val themeMode = dashboardUiState.themeMode()

    DashboardTheme(themeMode = themeMode) {
        DashboardTemplate(
            uiState = dashboardUiState,
            modifier = modifier,
            onRetry = onRetry
        )
    }
}

private fun DashboardUiState.themeMode(): DashboardThemeMode {
    return when (this) {
        DashboardUiState.Loading -> DashboardThemeMode.Day
        is DashboardUiState.Error -> DashboardThemeMode.Day
        is DashboardUiState.Content -> presentation.themeMode
    }
}
