package shared.data.repository

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import shared.domain.model.DomainError
import shared.domain.model.DomainException
import shared.domain.model.GeoCoordinates
import shared.fake.FakeAirQualityRemoteDataSource

class AirQualityRepositoryImplErrorTest {

    @Test
    fun should_return_domain_error_when_remote_fails() = runTest {
        val coordinates = GeoCoordinates(latitude = 10.0, longitude = 20.0)
        val error = IllegalStateException("boom")
        val remote = FakeAirQualityRemoteDataSource(exceptionToThrow = error)
        val repository = AirQualityRepositoryImpl(remote)

        val result = repository.getAirQuality(coordinates)

        assertTrue(result.isFailure)
        val exception = assertIs<DomainException>(result.exceptionOrNull())
        val failure = assertIs<DomainError.AirQualityFetchFailed>(exception.domainError)
        assertEquals(error, failure.cause)
        assertEquals(1, remote.calls)
        assertEquals(coordinates, remote.lastCoordinates)
    }
}