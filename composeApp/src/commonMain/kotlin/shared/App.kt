package shared

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import shared.domain.model.DayNight
import shared.presentation.dashboard.DashboardTheme
import shared.presentation.dashboard.dayNight
import shared.presentation.dashboard.toDashboardUiState
import shared.presentation.screen.DashboardRoute
import shared.presentation.screen.LocationsScreen
import shared.presentation.state.UiState
import shared.presentation.viewmodel.MainViewModel
import shared.presentation.viewmodel.DashboardViewModel
import shared.presentation.viewmodel.LocationsViewModel

@Composable
fun App(
    mainViewModel: MainViewModel,
    dashboardViewModel: DashboardViewModel,
    locationsViewModel: LocationsViewModel,
    modifier: Modifier = Modifier
) {
    val mainState = mainViewModel.uiState.collectAsState().value
    val dashboardStates = dashboardViewModel.dashboardStates.collectAsState().value
    val dashboardLocationStates = dashboardViewModel.locationStates.collectAsState().value
    val currentPageCoordinates = when (val currentLocationState = mainState.currentLocationState) {
        UiState.Loading -> null
        is UiState.Error -> null
        is UiState.Success -> buildList {
            add(currentLocationState.data)
            mainState.locations
                .map { it.coordinates }
                .filterNot { it == currentLocationState.data }
                .forEach { add(it) }
        }.let { pageCoordinates ->
            pageCoordinates[mainState.currentDashboardPage.coerceIn(0, pageCoordinates.lastIndex)]
        }
    }
    val themeMode = currentPageCoordinates?.let {
        (dashboardStates[it] ?: UiState.Loading)
            .toDashboardUiState(dashboardLocationStates[it] ?: UiState.Loading)
            .dayNight()
    } ?: DayNight.Night

    DashboardTheme(themeMode = themeMode) {
        Box(modifier = modifier.fillMaxSize()) {
            when (val currentLocationState = mainState.currentLocationState) {
                UiState.Loading -> Unit
                is UiState.Error -> LocationErrorView(
                    message = currentLocationState.message,
                    modifier = Modifier.fillMaxSize(),
                    onRetry = mainViewModel::refresh
                )
                is UiState.Success -> {
                    if (mainState.showLocationsScreen) {
                        LocationsScreen(
                            viewModel = locationsViewModel,
                            onBack = mainViewModel::closeLocationsScreen,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        DashboardRoute(
                            viewModel = dashboardViewModel,
                            coordinates = currentLocationState.data,
                            locations = mainState.locations,
                            refreshKey = mainState.refreshKey,
                            initialPage = mainState.currentDashboardPage,
                            onPageChanged = mainViewModel::onDashboardPageChanged,
                            onRefresh = mainViewModel::refresh,
                            onOpenLocationsWindow = mainViewModel::openLocationsScreen,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = mainState.currentLocationState == UiState.Loading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LocationLoadingView(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
private fun LocationLoadingView(modifier: Modifier = Modifier) {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.onSurface)
            Text(
                text = "Obteniendo ubicacion actual...",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun LocationErrorView(
    message: String,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge
        )
        Button(
            onClick = onRetry,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Reintentar")
        }
    }
}
