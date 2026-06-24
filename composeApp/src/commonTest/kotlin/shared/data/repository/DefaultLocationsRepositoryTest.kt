package shared.data.repository

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import shared.domain.model.GeoCoordinates
import shared.domain.model.LocationData

class DefaultLocationsRepositoryTest {

    @Test
    fun should_return_current_location_first_and_keep_saved_locations() {
        val repository = DefaultLocationsRepository()
        val currentLocation = buildLocation(
            latitude = -38.7167,
            longitude = -62.2833,
            displayName = "Bahia Blanca"
        )
        val savedLocation = buildLocation(
            latitude = -34.6037,
            longitude = -58.3816,
            displayName = "Buenos Aires"
        )

        repository.setCurrentLocation(currentLocation)
        repository.saveLocation(savedLocation)

        assertEquals(listOf(currentLocation, savedLocation), repository.getLocations())
    }

    @Test
    fun should_not_duplicate_current_location_when_saved() {
        val repository = DefaultLocationsRepository()
        val currentLocation = buildLocation(
            latitude = -38.7167,
            longitude = -62.2833,
            displayName = "Bahia Blanca"
        )

        repository.setCurrentLocation(currentLocation)
        repository.saveLocation(currentLocation)

        assertEquals(listOf(currentLocation), repository.getLocations())
    }

    @Test
    fun should_remove_only_saved_locations() {
        val repository = DefaultLocationsRepository()
        val currentLocation = buildLocation(
            latitude = -38.7167,
            longitude = -62.2833,
            displayName = "Bahia Blanca"
        )
        val savedLocation = buildLocation(
            latitude = -34.6037,
            longitude = -58.3816,
            displayName = "Buenos Aires"
        )

        repository.setCurrentLocation(currentLocation)
        repository.saveLocation(savedLocation)
        repository.removeLocation(savedLocation.coordinates)

        assertEquals(listOf(currentLocation), repository.getLocations())
        assertSame(currentLocation, repository.getCurrentLocation())
    }

    @Test
    fun should_load_saved_locations_from_storage() {
        val savedLocation = buildLocation(
            latitude = -34.6037,
            longitude = -58.3816,
            displayName = "Buenos Aires"
        )
        val storage = FakeLocationsStorage(initialLocations = listOf(savedLocation))

        val repository = DefaultLocationsRepository(storage)

        assertEquals(listOf(savedLocation), repository.getLocations())
    }

    @Test
    fun should_persist_saved_locations_when_changed() {
        val storage = FakeLocationsStorage()
        val repository = DefaultLocationsRepository(storage)
        val savedLocation = buildLocation(
            latitude = -34.6037,
            longitude = -58.3816,
            displayName = "Buenos Aires"
        )

        repository.saveLocation(savedLocation)

        assertEquals(listOf(savedLocation), storage.persistedLocations)

        repository.removeLocation(savedLocation.coordinates)

        assertEquals(emptyList(), storage.persistedLocations)
    }

    private fun buildLocation(
        latitude: Double,
        longitude: Double,
        displayName: String
    ): LocationData {
        return LocationData(
            coordinates = GeoCoordinates(latitude = latitude, longitude = longitude),
            displayName = displayName,
            locality = displayName,
            region = "Buenos Aires",
            country = "Argentina",
            countryCode = "AR"
        )
    }

    private class FakeLocationsStorage(
        private val initialLocations: List<LocationData> = emptyList()
    ) : LocationsStorage {
        var persistedLocations: List<LocationData> = emptyList()

        override fun readSavedLocations(): List<LocationData> {
            return initialLocations
        }

        override fun writeSavedLocations(locations: List<LocationData>) {
            persistedLocations = locations
        }
    }
}
