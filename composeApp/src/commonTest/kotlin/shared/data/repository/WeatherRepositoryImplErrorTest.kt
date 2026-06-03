package shared.data.repository

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import shared.domain.model.DomainError
import shared.domain.model.GeoCoordinates
import shared.fake.FakeWeatherRemoteDataSource

class WeatherRepositoryImplErrorTest {

    @Test
    fun should_return_domain_error_when_remote_fails() = runTest {
        val coordinates = GeoCoordinates(latitude = 10.0, longitude = 20.0)
        val error = IllegalStateException("boom")
        val remote = FakeWeatherRemoteDataSource(exceptionToThrow = error)
        val repository = WeatherRepositoryImpl(remote)

        val result = repository.getCurrentWeather(coordinates)

        assertTrue(result.isFailure)
        val failure = assertIs<DomainError.DashboardFetchFailed>(result.exceptionOrNull())
        assertEquals(error, failure.cause)
        assertEquals(1, remote.calls)
        assertEquals(coordinates, remote.lastCoordinates)
    }
}
