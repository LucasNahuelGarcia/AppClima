package shared.presentation.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import shared.domain.model.GeoCoordinates
import shared.domain.repository.LocationsProvider
import shared.presentation.dashboard.DashboardRoute
import shared.presentation.state.UiState
import shared.presentation.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    coordinates: GeoCoordinates,
    locationsProvider: LocationsProvider,
    refreshKey: Int,
    onRefresh: () -> Unit,
    onOpenLocationsWindow: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dashboardStates = viewModel.dashboardStates.collectAsState().value
    val locationStates = viewModel.locationStates.collectAsState().value
    val locations = locationsProvider.locationsState.collectAsState().value
    val pageCoordinates = remember(coordinates, locations) {
        buildList {
            add(coordinates)
            locations
                .map { it.coordinates }
                .filterNot { it == coordinates }
                .forEach { add(it) }
        }
    }
    val pagerState = rememberPagerState(pageCount = { pageCoordinates.size })

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize()
    ) { page ->
        val pageLocation = pageCoordinates[page]

        DashboardRoute(
            state = dashboardStates[pageLocation] ?: UiState.Loading,
            locationState = locationStates[pageLocation] ?: UiState.Loading,
            coordinates = pageLocation,
            refreshKey = refreshKey,
            currentPage = pagerState.currentPage,
            pageCount = pageCoordinates.size,
            isCurrentPage = page == pagerState.currentPage,
            modifier = Modifier.fillMaxSize(),
            onLoadDashboard = {
                viewModel.loadDashboard(
                    coordinates = it,
                    updateCurrentLocation = it == coordinates
                )
            },
            onRefresh = onRefresh,
            onOpenLocationsWindow = onOpenLocationsWindow
        )
    }
}
