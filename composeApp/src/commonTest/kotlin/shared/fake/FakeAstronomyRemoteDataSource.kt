package shared.fake

import shared.data.datasource.remote.AstronomyRemoteDataSource
import shared.data.dto.NasaApodDto

class FakeAstronomyRemoteDataSource(
    private val dtoToReturn: NasaApodDto? = null,
    private val exceptionToThrow: Exception? = null
) : AstronomyRemoteDataSource {
    var calls = 0

    override suspend fun getAstronomyData(): NasaApodDto {
        calls++
        exceptionToThrow?.let { throw it }
        return dtoToReturn
            ?: throw IllegalStateException("Missing NasaApodDto")
    }
}
