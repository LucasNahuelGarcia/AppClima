package shared.presentation.locations

import shared.domain.model.GeoCoordinates
import shared.domain.model.LocationData

data class LocationsUiState(
    val locations: List<LocationData> = emptyList(),
    val currentLocationCoordinates: GeoCoordinates? = null,
    val isAddingLocation: Boolean = false,
    val latitudeText: String = "",
    val longitudeText: String = "",
    val addLocationError: String? = null,
    val isSavingLocation: Boolean = false
)
