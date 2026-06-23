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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import shared.presentation.screen.DashboardScreen
import shared.presentation.viewmodel.DashboardViewModel
import shared.domain.model.DayNight
import shared.domain.model.GeoCoordinates
import shared.domain.repository.LocationsProvider
import shared.domain.usecase.GetDeviceLocationUseCase
import shared.domain.usecase.GetReverseGeocodingUseCase
import shared.presentation.dashboard.DashboardTheme
import shared.presentation.dashboard.dayNight
import shared.presentation.dashboard.toDashboardUiState
import shared.presentation.screen.LocationsScreen
import shared.presentation.state.UiState

@Composable
fun App(
    dashboardViewModel: DashboardViewModel,
    getDeviceLocationUseCase: GetDeviceLocationUseCase,
    getReverseGeocodingUseCase: GetReverseGeocodingUseCase,
    locationsProvider: LocationsProvider,
    modifier: Modifier = Modifier
) {
    var refreshKey by remember { mutableIntStateOf(0) }
    var showLocationsScreen by remember { mutableStateOf(false) }
    var currentDashboardPage by remember { mutableIntStateOf(0) }
    val locationState by produceState<UiState<GeoCoordinates>>(initialValue = UiState.Loading, refreshKey) {
        value = getDeviceLocationUseCase()
            .fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "No se pudo obtener la ubicacion del dispositivo") }
            )
    }
    val dashboardStates = dashboardViewModel.dashboardStates.collectAsState().value
    val dashboardLocationStates = dashboardViewModel.locationStates.collectAsState().value
    val locations = locationsProvider.locationsState.collectAsState().value
    val currentPageCoordinates = when (val currentLocationState = locationState) {
        UiState.Loading -> null
        is UiState.Error -> null
        is UiState.Success -> buildList {
            add(currentLocationState.data)
            locations
                .map { it.coordinates }
                .filterNot { it == currentLocationState.data }
                .forEach { add(it) }
        }.let { pageCoordinates ->
            pageCoordinates[currentDashboardPage.coerceIn(0, pageCoordinates.lastIndex)]
        }
    }
    val themeMode = currentPageCoordinates?.let {
        (dashboardStates[it] ?: UiState.Loading)
            .toDashboardUiState(dashboardLocationStates[it] ?: UiState.Loading)
            .dayNight()
    } ?: DayNight.Night

    DashboardTheme(themeMode = themeMode) {
        Box(modifier = modifier.fillMaxSize()) {
            when (val currentLocationState = locationState) {
                UiState.Loading -> Unit
                is UiState.Error -> LocationErrorView(
                    message = currentLocationState.message,
                    modifier = Modifier.fillMaxSize(),
                    onRetry = { refreshKey += 1 }
                )
                is UiState.Success -> {
                    if (showLocationsScreen) {
                        LocationsScreen(
                            locationsProvider = locationsProvider,
                            getReverseGeocodingUseCase = getReverseGeocodingUseCase,
                            onBack = { showLocationsScreen = false },
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        DashboardScreen(
                            viewModel = dashboardViewModel,
                            coordinates = currentLocationState.data,
                            locationsProvider = locationsProvider,
                            refreshKey = refreshKey,
                            initialPage = currentDashboardPage,
                            onPageChanged = { currentDashboardPage = it },
                            onRefresh = { refreshKey += 1 },
                            onOpenLocationsWindow = { showLocationsScreen = true },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = locationState == UiState.Loading,
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
