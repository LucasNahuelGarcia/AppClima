package shared.data.repository

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import shared.domain.model.DomainError
import shared.domain.model.DomainException
import shared.domain.model.GeoCoordinates
import shared.fake.FakeReverseGeocodingRemoteDataSource

class ReverseGeocodingRepositoryImplErrorTest {

    @Test
    fun should_return_domain_error_when_remote_fails() = runTest {
        val coordinates = GeoCoordinates(latitude = 10.0, longitude = 20.0)
        val cause = IllegalStateException("network failed")
        val remote = FakeReverseGeocodingRemoteDataSource(exceptionToThrow = cause)
        val repository = ReverseGeocodingRepositoryImpl(remote)

        val result = repository.getLocation(coordinates)

        assertTrue(result.isFailure)
        val exception = assertIs<DomainException>(result.exceptionOrNull())
        val error = assertIs<DomainError.ReverseGeocodingFailed>(exception.domainError)
        assertEquals(cause, error.cause)
        assertEquals(1, remote.calls)
        assertEquals(coordinates, remote.lastCoordinates)
    }
}