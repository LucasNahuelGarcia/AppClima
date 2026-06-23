package shared.data.repository

import shared.domain.model.GeoCoordinates
import shared.domain.model.LocationData
import shared.domain.repository.LocationsProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryLocationsProvider : LocationsProvider {

    private var currentLocation: LocationData? = null
    private val savedLocations = mutableListOf<LocationData>()
    private val _locationsState = MutableStateFlow<List<LocationData>>(emptyList())

    override val locationsState: StateFlow<List<LocationData>> = _locationsState.asStateFlow()

    override fun getLocations(): List<LocationData> {
        return buildLocationsSnapshot()
    }

    override fun getCurrentLocation(): LocationData? {
        return currentLocation
    }

    override fun setCurrentLocation(location: LocationData) {
        currentLocation = location
        publishLocations()
    }

    override fun saveLocation(location: LocationData) {
        if (location.coordinates == currentLocation?.coordinates) {
            return
        }

        if (savedLocations.any { it.coordinates == location.coordinates }) {
            return
        }

        savedLocations.add(location)
        publishLocations()
    }

    override fun removeLocation(coordinates: GeoCoordinates) {
        if (currentLocation?.coordinates == coordinates) {
            return
        }

        savedLocations.removeAll { it.coordinates == coordinates }
        publishLocations()
    }

    override fun clearSavedLocations() {
        savedLocations.clear()
        publishLocations()
    }

    private fun publishLocations() {
        _locationsState.value = buildLocationsSnapshot()
    }

    private fun buildLocationsSnapshot(): List<LocationData> {
        return buildList {
            currentLocation?.let { add(it) }
            addAll(savedLocations.filterNot { it.coordinates == currentLocation?.coordinates })
        }
    }
}