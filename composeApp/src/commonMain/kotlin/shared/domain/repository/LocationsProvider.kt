package shared.domain.repository

import kotlinx.coroutines.flow.StateFlow
import shared.domain.model.GeoCoordinates
import shared.domain.model.LocationData

interface LocationsProvider {
    fun getLocations(): List<LocationData>
    val locationsState: StateFlow<List<LocationData>>
    fun getCurrentLocation(): LocationData?
    fun setCurrentLocation(location: LocationData)
    fun saveLocation(location: LocationData)
    fun removeLocation(coordinates: GeoCoordinates)
    fun clearSavedLocations()
}