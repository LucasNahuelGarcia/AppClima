package shared.presentation.screen

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.composed
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.launch
import shared.domain.model.GeoCoordinates
import shared.domain.model.LocationData
import shared.presentation.dashboard.DashboardRoute
import shared.presentation.state.UiState
import shared.presentation.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    coordinates: GeoCoordinates,
    locations: List<LocationData>,
    refreshKey: Int,
    initialPage: Int,
    onPageChanged: (Int) -> Unit,
    onRefresh: () -> Unit,
    onOpenLocationsWindow: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dashboardStates = viewModel.dashboardStates.collectAsState().value
    val locationStates = viewModel.locationStates.collectAsState().value
    val pageCoordinates = remember(coordinates, locations) {
        buildList {
            add(coordinates)
            locations
                .map { it.coordinates }
                .filterNot { it == coordinates }
                .forEach { add(it) }
        }
    }
    val pagerState = rememberPagerState(
        initialPage = initialPage.coerceIn(0, pageCoordinates.lastIndex),
        pageCount = { pageCoordinates.size }
    )

    LaunchedEffect(pagerState.currentPage) {
        onPageChanged(pagerState.currentPage)
    }

    LaunchedEffect(pageCoordinates.size) {
        val lastPage = pageCoordinates.lastIndex
        if (pagerState.currentPage > lastPage) {
            pagerState.scrollToPage(lastPage)
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize().enableDesktopDragScroll(pagerState)
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

fun Modifier.enableDesktopDragScroll(state: PagerState): Modifier = composed {
    val scope = rememberCoroutineScope()

    val snapToNearestPage: () -> Unit = {
        scope.launch {
            state.animateScrollToPage(state.currentPage)
        }
    }

    this.pointerInput(state) {
        detectHorizontalDragGestures(
            onDragEnd = snapToNearestPage,
            onDragCancel = snapToNearestPage,
            onHorizontalDrag = { change, dragAmount ->
                if (change.type == PointerType.Mouse) {
                    change.consume()
                    scope.launch {
                        state.scrollBy(-dragAmount)
                    }
                }
            }
        )
    }
}
