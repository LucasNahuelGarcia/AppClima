package shared.domain.usecase

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import shared.domain.model.GeoCoordinates
import shared.domain.model.LocationData
import shared.fake.FakeReverseGeocodingRepository

class GetReverseGeocodingUseCaseImplTest {

    @Test
    fun should_return_location_from_repository() = runTest {
        val coordinates = GeoCoordinates(latitude = -38.7167, longitude = -62.2833)
        val location = LocationData(
            coordinates = coordinates,
            displayName = "Bahia Blanca, Buenos Aires, Argentina",
            locality = "Bahia Blanca",
            region = "Buenos Aires",
            country = "Argentina",
            countryCode = "ar"
        )
        val repository = FakeReverseGeocodingRepository(Result.success(location))
        val useCase = GetReverseGeocodingUseCaseImpl(repository)

        val result = useCase(coordinates)

        assertTrue(result.isSuccess)
        assertEquals(location, result.getOrThrow())
        assertEquals(1, repository.calls)
        assertEquals(coordinates, repository.lastCoordinates)
    }

    @Test
    fun should_return_failure_from_repository() = runTest {
        val coordinates = GeoCoordinates(latitude = 10.0, longitude = 20.0)
        val error = IllegalStateException("reverse geocoding failed")
        val repository = FakeReverseGeocodingRepository(Result.failure(error))
        val useCase = GetReverseGeocodingUseCaseImpl(repository)

        val result = useCase(coordinates)

        assertTrue(result.isFailure)
        assertEquals(error, result.exceptionOrNull())
        assertEquals(1, repository.calls)
        assertEquals(coordinates, repository.lastCoordinates)
    }
}
