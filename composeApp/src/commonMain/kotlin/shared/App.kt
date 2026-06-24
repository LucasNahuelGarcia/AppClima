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
import shared.presentation.screen.DashboardScreen
import shared.presentation.screen.LocationsScreen
import shared.presentation.state.UiState
import shared.presentation.viewmodel.AppViewModel
import shared.presentation.viewmodel.DashboardViewModel
import shared.presentation.viewmodel.LocationsViewModel

@Composable
fun App(
    appViewModel: AppViewModel,
    dashboardViewModel: DashboardViewModel,
    locationsViewModel: LocationsViewModel,
    modifier: Modifier = Modifier
) {
    val appState = appViewModel.uiState.collectAsState().value
    val dashboardStates = dashboardViewModel.dashboardStates.collectAsState().value
    val dashboardLocationStates = dashboardViewModel.locationStates.collectAsState().value
    val currentPageCoordinates = when (val currentLocationState = appState.currentLocationState) {
        UiState.Loading -> null
        is UiState.Error -> null
        is UiState.Success -> buildList {
            add(currentLocationState.data)
            appState.locations
                .map { it.coordinates }
                .filterNot { it == currentLocationState.data }
                .forEach { add(it) }
        }.let { pageCoordinates ->
            pageCoordinates[appState.currentDashboardPage.coerceIn(0, pageCoordinates.lastIndex)]
        }
    }
    val themeMode = currentPageCoordinates?.let {
        (dashboardStates[it] ?: UiState.Loading)
            .toDashboardUiState(dashboardLocationStates[it] ?: UiState.Loading)
            .dayNight()
    } ?: DayNight.Night

    DashboardTheme(themeMode = themeMode) {
        Box(modifier = modifier.fillMaxSize()) {
            when (val currentLocationState = appState.currentLocationState) {
                UiState.Loading -> Unit
                is UiState.Error -> LocationErrorView(
                    message = currentLocationState.message,
                    modifier = Modifier.fillMaxSize(),
                    onRetry = appViewModel::refresh
                )
                is UiState.Success -> {
                    if (appState.showLocationsScreen) {
                        LocationsScreen(
                            viewModel = locationsViewModel,
                            onBack = appViewModel::closeLocationsScreen,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        DashboardScreen(
                            viewModel = dashboardViewModel,
                            coordinates = currentLocationState.data,
                            locations = appState.locations,
                            refreshKey = appState.refreshKey,
                            initialPage = appState.currentDashboardPage,
                            onPageChanged = appViewModel::onDashboardPageChanged,
                            onRefresh = appViewModel::refresh,
                            onOpenLocationsWindow = appViewModel::openLocationsScreen,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = appState.currentLocationState == UiState.Loading,
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
