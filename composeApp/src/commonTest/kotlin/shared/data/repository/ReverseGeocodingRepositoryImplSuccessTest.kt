package shared.data.repository

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import shared.data.dto.NominatimAddressDto
import shared.data.dto.NominatimReverseGeocodingDto
import shared.domain.model.GeoCoordinates
import shared.domain.model.LocationData
import shared.fake.FakeReverseGeocodingRemoteDataSource

class ReverseGeocodingRepositoryImplSuccessTest {

    @Test
    fun should_return_location_when_remote_ok() = runTest {
        val coordinates = GeoCoordinates(latitude = -38.7167, longitude = -62.2833)
        val dto = NominatimReverseGeocodingDto(
            displayName = "Bahia Blanca, Buenos Aires, Argentina",
            address = NominatimAddressDto(
                city = "Bahia Blanca",
                state = "Buenos Aires",
                country = "Argentina",
                countryCode = "ar"
            )
        )
        val remote = FakeReverseGeocodingRemoteDataSource(dtoToReturn = dto)
        val repository = ReverseGeocodingRepositoryImpl(remote)

        val result = repository.getLocation(coordinates)

        assertTrue(result.isSuccess)
        assertEquals(
            LocationData(
                coordinates = coordinates,
                displayName = "Bahia Blanca, Buenos Aires, Argentina",
                locality = "Bahia Blanca",
                region = "Buenos Aires",
                country = "Argentina",
                countryCode = "ar"
            ),
            result.getOrThrow()
        )
        assertEquals(1, remote.calls)
        assertEquals(coordinates, remote.lastCoordinates)
    }
}
