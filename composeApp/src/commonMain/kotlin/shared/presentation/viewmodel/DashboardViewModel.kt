package shared.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import shared.domain.model.DashboardData
import shared.domain.model.GeoCoordinates
import shared.domain.model.LocationData
import shared.domain.usecase.GetDashboardDataUseCase
import shared.domain.usecase.GetReverseGeocodingUseCase
import shared.presentation.state.UiState

class DashboardViewModel(
    private val getDashboardDataUseCase: GetDashboardDataUseCase,
    private val getReverseGeocodingUseCase: GetReverseGeocodingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<DashboardData>>(UiState.Loading)
    val uiState: StateFlow<UiState<DashboardData>> = _uiState.asStateFlow()

    private val _locationState = MutableStateFlow<UiState<LocationData>>(UiState.Loading)
    val locationState: StateFlow<UiState<LocationData>> = _locationState.asStateFlow()

    private var lastCoordinates: GeoCoordinates? = null

    fun loadDashboard(coordinates: GeoCoordinates) {
        lastCoordinates = coordinates
        loadLocation(coordinates)
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = getDashboardDataUseCase(coordinates)
            _uiState.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Dashboard fetch failed") }
            )
        }
    }

    fun retry() {
        lastCoordinates?.let { loadDashboard(it) }
    }

    private fun loadLocation(coordinates: GeoCoordinates) {
        viewModelScope.launch {
            _locationState.value = UiState.Loading
            val result = getReverseGeocodingUseCase(coordinates)
            _locationState.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Reverse geocoding failed") }
            )
        }
    }
}
