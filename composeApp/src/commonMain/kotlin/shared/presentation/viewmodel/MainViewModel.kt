package shared.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import shared.domain.repository.LocationsRepository
import shared.domain.usecase.GetApproximateDeviceLocationUseCase
import shared.presentation.app.MainUiState
import shared.presentation.state.UiState

class MainViewModel(
    private val getApproximateDeviceLocationUseCase: GetApproximateDeviceLocationUseCase,
    private val locationsRepository: LocationsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        MainUiState(locations = locationsRepository.locationsState.value)
    )
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        loadCurrentLocation()
        viewModelScope.launch {
            locationsRepository.locationsState.collect { locations ->
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
            val state = getApproximateDeviceLocationUseCase().fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "No se pudo obtener la ubicacion del dispositivo") }
            )
            updateState { copy(currentLocationState = state) }
        }
    }

    private fun updateState(update: MainUiState.() -> MainUiState) {
        _uiState.value = _uiState.value.update()
    }
}
