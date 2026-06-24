package shared.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import shared.domain.repository.LocationsProvider
import shared.domain.usecase.GetDeviceLocationUseCase
import shared.presentation.app.AppUiState
import shared.presentation.state.UiState

class AppViewModel(
    private val getDeviceLocationUseCase: GetDeviceLocationUseCase,
    private val locationsProvider: LocationsProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AppUiState(locations = locationsProvider.locationsState.value)
    )
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    init {
        loadCurrentLocation()
        viewModelScope.launch {
            locationsProvider.locationsState.collect { locations ->
                updateState { copy(locations = locations) }
            }
        }
    }

    fun refresh() {
        updateState { copy(refreshKey = refreshKey + 1) }
        loadCurrentLocation()
    }

    fun openLocationsScreen() {
        updateState { copy(showLocationsScreen = true) }
    }

    fun closeLocationsScreen() {
        updateState { copy(showLocationsScreen = false) }
    }

    fun onDashboardPageChanged(page: Int) {
        updateState { copy(currentDashboardPage = page) }
    }

    private fun loadCurrentLocation() {
        viewModelScope.launch {
            updateState { copy(currentLocationState = UiState.Loading) }
            val state = getDeviceLocationUseCase().fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "No se pudo obtener la ubicacion del dispositivo") }
            )
            updateState { copy(currentLocationState = state) }
        }
    }

    private fun updateState(update: AppUiState.() -> AppUiState) {
        _uiState.value = _uiState.value.update()
    }
}
