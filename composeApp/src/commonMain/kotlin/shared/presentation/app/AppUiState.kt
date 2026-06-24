package shared.presentation.app

import shared.domain.model.GeoCoordinates
import shared.domain.model.LocationData
import shared.presentation.state.UiState

data class AppUiState(
    val currentLocationState: UiState<GeoCoordinates> = UiState.Loading,
    val locations: List<LocationData> = emptyList(),
    val refreshKey: Int = 0,
    val showLocationsScreen: Boolean = false,
    val currentDashboardPage: Int = 0
)
