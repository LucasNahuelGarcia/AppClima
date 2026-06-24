package shared.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import shared.domain.model.GeoCoordinates
import shared.domain.repository.LocationsProvider
import shared.domain.usecase.GetReverseGeocodingUseCase
import shared.presentation.locations.LocationsUiState

class LocationsViewModel(
    private val locationsProvider: LocationsProvider,
    private val getReverseGeocodingUseCase: GetReverseGeocodingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(buildUiState())
    val uiState: StateFlow<LocationsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            locationsProvider.locationsState.collect { locations ->
                updateState {
                    copy(
                        locations = locations,
                        currentLocationCoordinates = locationsProvider.getCurrentLocation()?.coordinates
                    )
                }
            }
        }
    }

    fun startAddingLocation() {
        updateState {
            copy(
                isAddingLocation = true,
                addLocationError = null
            )
        }
    }

    fun onLatitudeChange(value: String) {
        updateState {
            copy(
                latitudeText = value,
                addLocationError = null
            )
        }
    }

    fun onLongitudeChange(value: String) {
        updateState {
            copy(
                longitudeText = value,
                addLocationError = null
            )
        }
    }

    fun cancelAddLocation() {
        clearAddLocationForm()
    }

    fun removeLocation(coordinates: GeoCoordinates) {
        locationsProvider.removeLocation(coordinates)
    }

    fun saveLocation() {
        val latitude = uiState.value.latitudeText.toDoubleOrNull()
        val longitude = uiState.value.longitudeText.toDoubleOrNull()

        if (latitude == null || longitude == null) {
            updateState { copy(addLocationError = "Ingresá latitud y longitud válidas") }
            return
        }

        viewModelScope.launch {
            updateState {
                copy(
                    isSavingLocation = true,
                    addLocationError = null
                )
            }
            getReverseGeocodingUseCase(
                GeoCoordinates(
                    latitude = latitude,
                    longitude = longitude
                )
            ).fold(
                onSuccess = {
                    locationsProvider.saveLocation(it)
                    clearAddLocationForm()
                },
                onFailure = {
                    updateState {
                        copy(
                            addLocationError = it.message ?: "No se pudo guardar la ubicación"
                        )
                    }
                }
            )
            updateState { copy(isSavingLocation = false) }
        }
    }

    private fun clearAddLocationForm() {
        updateState {
            copy(
                latitudeText = "",
                longitudeText = "",
                addLocationError = null,
                isAddingLocation = false
            )
        }
    }

    private fun buildUiState(): LocationsUiState {
        return LocationsUiState(
            locations = locationsProvider.locationsState.value,
            currentLocationCoordinates = locationsProvider.getCurrentLocation()?.coordinates
        )
    }

    private fun updateState(update: LocationsUiState.() -> LocationsUiState) {
        _uiState.value = _uiState.value.update()
    }
}
