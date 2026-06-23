package shared.data.repository

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import shared.domain.model.GeoCoordinates
import shared.domain.model.LocationData

class InMemoryLocationsProviderTest {

    @Test
    fun should_return_current_location_first_and_keep_saved_locations() {
        val provider = InMemoryLocationsProvider()
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

        provider.setCurrentLocation(currentLocation)
        provider.saveLocation(savedLocation)

        assertEquals(listOf(currentLocation, savedLocation), provider.getLocations())
    }

    @Test
    fun should_not_duplicate_current_location_when_saved() {
        val provider = InMemoryLocationsProvider()
        val currentLocation = buildLocation(
            latitude = -38.7167,
            longitude = -62.2833,
            displayName = "Bahia Blanca"
        )

        provider.setCurrentLocation(currentLocation)
        provider.saveLocation(currentLocation)

        assertEquals(listOf(currentLocation), provider.getLocations())
    }

    @Test
    fun should_remove_only_saved_locations() {
        val provider = InMemoryLocationsProvider()
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

        provider.setCurrentLocation(currentLocation)
        provider.saveLocation(savedLocation)
        provider.removeLocation(savedLocation.coordinates)

        assertEquals(listOf(currentLocation), provider.getLocations())
        assertSame(currentLocation, provider.getCurrentLocation())
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
}