package shared.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.datetime.Clock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import shared.domain.model.DashboardData
import shared.domain.model.DomainError
import shared.domain.model.DomainException
import shared.domain.model.GeoCoordinates
import shared.domain.model.LocationData
import shared.domain.repository.LocationsRepository
import shared.domain.usecase.GetDashboardDataUseCase
import shared.domain.usecase.ResolveLocationUseCase
import shared.presentation.state.UiState

class DashboardViewModel(
    private val getDashboardDataUseCase: GetDashboardDataUseCase,
    private val resolveLocationUseCase: ResolveLocationUseCase,
    private val locationsRepository: LocationsRepository
) : ViewModel() {

    private companion object {
        const val DASHBOARD_REFRESH_INTERVAL_MILLIS = 60_000L
    }

    private val _uiState = MutableStateFlow<UiState<DashboardData>>(UiState.Loading)
    val uiState: StateFlow<UiState<DashboardData>> = _uiState.asStateFlow()

    private val _locationState = MutableStateFlow<UiState<LocationData>>(UiState.Loading)
    val locationState: StateFlow<UiState<LocationData>> = _locationState.asStateFlow()

    private val _dashboardStates = MutableStateFlow<Map<GeoCoordinates, UiState<DashboardData>>>(emptyMap())
    val dashboardStates: StateFlow<Map<GeoCoordinates, UiState<DashboardData>>> = _dashboardStates.asStateFlow()

    private val _locationStates = MutableStateFlow<Map<GeoCoordinates, UiState<LocationData>>>(emptyMap())
    val locationStates: StateFlow<Map<GeoCoordinates, UiState<LocationData>>> = _locationStates.asStateFlow()

    private val dashboardLoadedAtMillis = mutableMapOf<GeoCoordinates, Long>()

    private var lastCoordinates: GeoCoordinates? = null
    private var lastUpdateCurrentLocation: Boolean = true

    fun loadDashboard(
        coordinates: GeoCoordinates,
        updateCurrentLocation: Boolean = true
    ) {
        lastCoordinates = coordinates
        lastUpdateCurrentLocation = updateCurrentLocation

        if (hasFreshDashboardData(coordinates)) {
            return
        }

        loadLocation(
            coordinates = coordinates,
            updateCurrentLocation = updateCurrentLocation
        )
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _dashboardStates.value = _dashboardStates.value + (coordinates to UiState.Loading)
            val result = getDashboardDataUseCase(coordinates)
            val state = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { throwable ->
                    val domainError = (throwable as? DomainException)?.domainError
                    UiState.Error(domainError?.message ?: "Dashboard fetch failed")
                }
            )
            _uiState.value = state
            _dashboardStates.value = _dashboardStates.value + (coordinates to state)
            if (state is UiState.Success) {
                dashboardLoadedAtMillis[coordinates] = Clock.System.now().toEpochMilliseconds()
            }
        }
    }

    fun retry() {
        lastCoordinates?.let {
            loadDashboard(
                coordinates = it,
                updateCurrentLocation = lastUpdateCurrentLocation
            )
        }
    }

    private fun loadLocation(
        coordinates: GeoCoordinates,
        updateCurrentLocation: Boolean
    ) {
        viewModelScope.launch {
            _locationState.value = UiState.Loading
            _locationStates.value = _locationStates.value + (coordinates to UiState.Loading)
            val result = resolveLocationUseCase(coordinates)
            val state = result.fold(
                onSuccess = {
                    if (updateCurrentLocation) {
                        locationsRepository.setCurrentLocation(it)
                    }
                    UiState.Success(it)
                },
                onFailure = { throwable ->
                    val domainError = (throwable as? DomainException)?.domainError
                    if (domainError is DomainError.UnableToGeocode) {
                        removeUnableToGeocodeLocation(coordinates)
                    }
                    UiState.Error(domainError?.message ?: "Reverse geocoding failed")
                }
            )
            _locationState.value = state
            if (shouldKeepLocationState(state)) {
                _locationStates.value = _locationStates.value + (coordinates to state)
            }
        }
    }

    private fun removeUnableToGeocodeLocation(coordinates: GeoCoordinates) {
        locationsRepository.removeLocation(coordinates)
        _dashboardStates.value = _dashboardStates.value - coordinates
        _locationStates.value = _locationStates.value - coordinates
        dashboardLoadedAtMillis.remove(coordinates)
    }

    private fun shouldKeepLocationState(state: UiState<LocationData>): Boolean {
        return state !is UiState.Error || (state.message != DomainError.UnableToGeocode.message)
    }

    private fun hasFreshDashboardData(coordinates: GeoCoordinates): Boolean {
        if (_dashboardStates.value[coordinates] !is UiState.Success) {
            return false
        }

        val loadedAtMillis = dashboardLoadedAtMillis[coordinates] ?: return false
        val elapsedMillis = Clock.System.now().toEpochMilliseconds() - loadedAtMillis
        return elapsedMillis < DASHBOARD_REFRESH_INTERVAL_MILLIS
    }
}