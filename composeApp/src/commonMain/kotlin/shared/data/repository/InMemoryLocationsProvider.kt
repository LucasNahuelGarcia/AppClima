package shared.data.repository

import shared.domain.model.GeoCoordinates
import shared.domain.model.LocationData
import shared.domain.repository.LocationsProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryLocationsProvider(
    private val storage: LocationsStorage = NoOpLocationsStorage()
) : LocationsProvider {

    private var currentLocation: LocationData? = null
    private val savedLocations = storage.readSavedLocations().toMutableList()
    private val _locationsState = MutableStateFlow<List<LocationData>>(emptyList())

    override val locationsState: StateFlow<List<LocationData>> = _locationsState.asStateFlow()

    init {
        publishLocations()
    }

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
        persistSavedLocations()
        publishLocations()
    }

    override fun removeLocation(coordinates: GeoCoordinates) {
        if (currentLocation?.coordinates == coordinates) {
            return
        }

        savedLocations.removeAll { it.coordinates == coordinates }
        persistSavedLocations()
        publishLocations()
    }

    override fun clearSavedLocations() {
        savedLocations.clear()
        persistSavedLocations()
        publishLocations()
    }

    private fun persistSavedLocations() {
        storage.writeSavedLocations(savedLocations)
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
