package shared.data.repository

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import shared.domain.model.DomainError
import shared.domain.model.DomainException
import shared.fake.FakeApproximateDeviceLocationRemoteDataSource

class ApproximateDeviceLocationRepositoryImplErrorTest {

    @Test
    fun should_return_domain_error_when_remote_fails() = runTest {
        val error = IllegalStateException("device location fetch failed")
        val remote = FakeApproximateDeviceLocationRemoteDataSource(exceptionToThrow = error)
        val repository = ApproximateDeviceLocationRepositoryImpl(remote)

        val result = repository.getApproximateCoordinates()

        assertTrue(result.isFailure)
        val exception = assertIs<DomainException>(result.exceptionOrNull())
        val failure = assertIs<DomainError.LocationFetchFailed>(exception.domainError)
        assertEquals(error, failure.cause)
        assertEquals(1, remote.calls)
    }
}
