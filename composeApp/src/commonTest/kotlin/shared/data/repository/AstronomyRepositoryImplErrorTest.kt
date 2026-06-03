package shared.data.repository

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import shared.domain.model.DomainError
import shared.fake.FakeAstronomyRemoteDataSource

class AstronomyRepositoryImplErrorTest {

    @Test
    fun should_return_domain_error_when_remote_fails() = runTest {
        val error = IllegalArgumentException("boom")
        val remote = FakeAstronomyRemoteDataSource(exceptionToThrow = error)
        val repository = AstronomyRepositoryImpl(remote)

        val result = repository.getAstronomyData()

        assertTrue(result.isFailure)
        val failure = assertIs<DomainError.DashboardFetchFailed>(result.exceptionOrNull())
        assertEquals(error, failure.cause)
        assertEquals(1, remote.calls)
    }
}
