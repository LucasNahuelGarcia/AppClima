package shared.data.repository

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import shared.data.dto.NasaApodDto
import shared.domain.model.AstronomyData
import shared.fake.FakeAstronomyRemoteDataSource

class AstronomyRepositoryImplSuccessTest {

    @Test
    fun should_return_astronomy_when_remote_ok() = runTest {
        val dto = NasaApodDto(
            title = "Milky Way",
            date = "2026-06-02",
            explanation = "Night sky view",
            mediaType = "image",
            url = "https://example.com/apod.jpg",
            hdUrl = "https://example.com/apod_hd.jpg"
        )
        val remote = FakeAstronomyRemoteDataSource(dtoToReturn = dto)
        val repository = AstronomyRepositoryImpl(remote)

        val result = repository.getAstronomyData()

        assertTrue(result.isSuccess)
        assertEquals(
            AstronomyData(
                title = "Milky Way",
                date = "2026-06-02",
                explanation = "Night sky view",
                mediaType = "image",
                url = "https://example.com/apod.jpg",
                hdUrl = "https://example.com/apod_hd.jpg"
            ),
            result.getOrThrow()
        )
        assertEquals(1, remote.calls)
    }
}
